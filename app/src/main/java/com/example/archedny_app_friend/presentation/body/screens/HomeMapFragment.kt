package com.example.archedny_app_friend.presentation.body.screens

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.archedny_app_friend.R
import com.example.archedny_app_friend.databinding.FragmentHomeMapBinding
import com.example.archedny_app_friend.domain.models.MyLatLang
import com.example.archedny_app_friend.domain.models.User
import com.example.archedny_app_friend.presentation.MainActivity
import com.example.archedny_app_friend.presentation.auth.AuthViewModel
import com.example.archedny_app_friend.presentation.body.adapters.FriendItemAdapter
import com.example.archedny_app_friend.presentation.body.viewmodels.HomeViewModel
import com.example.archedny_app_friend.presentation.body.viewmodels.SharedViewModel
import com.example.archedny_app_friend.services.TrackingService
import com.example.archedny_app_friend.utils.Constants
import com.example.archedny_app_friend.utils.PermissionUtitlity
import com.example.archedny_app_friend.utils.ResultState
import com.example.archedny_app_friend.utils.myextention.ToastType
import com.example.archedny_app_friend.utils.myextention.coustomToast
import com.example.archedny_app_friend.utils.myextention.toast
import com.example.archedny_app_friend.utils.out
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject


@AndroidEntryPoint
class HomeMapFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    private var _binding: FragmentHomeMapBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel>()
    private val sharedViewModel by viewModels<SharedViewModel>()
    @Inject
    lateinit var friendItemAdapter:FriendItemAdapter

    private lateinit var mGoogleMap: GoogleMap
    private var friendYouWentShare:User?=null
    private var myMarker:Marker?=null
    private var friendMarker:Marker?=null
    private lateinit var myLatLang:MutableStateFlow<LatLng?>
    private lateinit var friendLatLang:MutableStateFlow<LatLng?>
    private lateinit var polylineOption:PolylineOptions

    private var isMapExtend=false

    var jopMapMove: Job? = null
    private var isDrawerOpen=false

    private val callback = OnMapReadyCallback { googleMap ->
        mGoogleMap = googleMap
        setUpLatLangUsersAndObservers()
        observerCurrentLocationFromServices()
        observeMapToolIsOutoExtended()

    }

    //-----------------------------------lifecyle / components----------------------------
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeMapBinding.inflate(layoutInflater)
        setUp()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if (PermissionUtitlity.hasLocationPermissin(requireContext())) {
            binding.tvPermission.visibility = View.GONE
        } else {
            binding.tvPermission.visibility = View.VISIBLE
        }
    }



    //-----------------------------------setups----------------------------
    private fun setUp() {
        if (PermissionUtitlity.hasLocationPermissin(requireContext())) {
            binding.tvPermission.visibility = View.GONE
        } else {
            binding.tvPermission.visibility = View.VISIBLE
        }

        PermissionUtitlity.requestLocationPermission(
            context = requireContext(),
            fragment = this,
            requestCode = Constants.REQUEST_CODE_LOCATION,
        )
        homeViewModel.getMyFreiends()
        setUpView()
        setUpViewAction()
        setUpObservers()

    }

    private fun setUpView() {
        setUpRecyclerView()
    }

    private fun setUpViewAction() {
        binding.imgMenuDrawer.setOnClickListener {
            if (isDrawerOpen){
                MainActivity.closeNavDraw()
                isDrawerOpen=false
            }else{
                MainActivity.openNavDraw()
                isDrawerOpen=true
            }

        }
        binding.fabStartPauseTrack.setOnClickListener {
            if (TrackingService.isTracking.value){
                endTracking()
            }else{
                startTracking()
            }

        }
        binding.fabMapState.setOnClickListener {
            if (isMapExtend){
                binding.friendsContainer.visibility = View.VISIBLE
                binding.fabMapState.rotation = 180f
                isMapExtend=false
            }else{
                binding.friendsContainer.visibility = View.GONE
                binding.fabMapState.rotation = 0f
                isMapExtend=true
            }
        }
        binding.fabSearch.setOnClickListener {
            findNavController().navigate(R.id.action_homeMapFragment2_to_searshFragment)
        }
        friendItemAdapter.setOnItemClickListener { user ->
            friendYouWentShare=user
            homeViewModel.getFriendLocation(user.id!!)
        }
    }

    private fun endTracking() {
        sendActionToService(
            Constants.STOP_SERVICES
        )
        TrackingService.isTracking.value=false
        binding.imgUpload.isVisible=false
        binding.imgDownload.isVisible=false
    }

    private fun startTracking() {
        if (friendYouWentShare==null){
            coustomToast(
                type = ToastType.WARNING,
                msg = "Please Select Freind To Share Location with him",
                context = requireContext()
            )
            return
        }
        binding.imgUpload.isVisible=true
        sendActionToService(
            Constants.START_SERVICES
        )
        TrackingService.isTracking.value=true
        binding.imgDownload.isVisible=true
    }

    private fun setUpObservers() {
        observerIsTrackingFromServices()
        observersUsersFreindFromHomeViewModel()
        observerShareLocationFromHomeViewModel()
        observerYourFreindLocation()

    }

    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            adapter = friendItemAdapter
        }
    }



    fun setUpLatLangUsersAndObservers(){
        myLatLang = MutableStateFlow(null)
        var myPolyLine:Polyline?=null
        friendLatLang = MutableStateFlow(null)
        var friendPolyLine:Polyline?=null
        lifecycleScope.launchWhenStarted{
            myLatLang.collect { my->
                my?.let {
                    friendLatLang.value.let {friend ->
                        friendPolyLine?.remove()
                        myPolyLine?.remove()
                        polylineOption = PolylineOptions()
                            .color(R.color.purple_500)
                            .width(12f)
                            .add(my)
                            .add(friend)
                        myPolyLine=mGoogleMap.addPolyline(polylineOption)
                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted{
            friendLatLang.collect { friend->
                friend?.let {
                    myLatLang.value?.let {my->
                        myPolyLine?.remove()
                        friendPolyLine?.remove()
                        polylineOption = PolylineOptions()
                            .color(R.color.purple_500)
                            .width(12f)
                            .add(friend)
                            .add(my)
                        friendPolyLine=mGoogleMap.addPolyline(polylineOption)
                    }
                }
            }
        }
    }


    // --------------------------actions------------------------------

    fun sendActionToService(action: String) {
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }
    }

    // --------------------------observers------------------------------

    private fun observersUsersFreindFromHomeViewModel() {
        lifecycleScope.launchWhenStarted {
            homeViewModel.users.collect {
                when {
                    it is ResultState.IsLoading -> {
                        binding.pbFrHome.isVisible = true
                        binding.tvError.isVisible = false
                    }
                    it is ResultState.IsSucsses -> {
                        binding.pbFrHome.isVisible = false
                        binding.tvError.isVisible = false
                        friendItemAdapter.setData((it.data!!))
                    }

                    it is ResultState.IsError -> {
                        binding.pbFrHome.isVisible = false
                        binding.tvError.text = it.message!!
                        binding.tvError.isVisible = true
                    }
                }
            }
        }
    }

    private fun observerCurrentLocationFromServices() {
        lifecycleScope.launchWhenStarted {
            TrackingService.currentLocation.collect { location ->
                location?.let {
                    myMarker?.remove()
                    myMarker=mGoogleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(it.latitude, it.longitude))
                            .title("me")
                            .icon(bitmapDescriptorFromVector(requireContext(),R.drawable.ic_run))
                    )
                    myLatLang.value=LatLng(it.latitude, it.longitude)
//                    mGoogleMap.moveCamera(
//                        CameraUpdateFactory.newLatLng(
//                            LatLng(
//                                it.latitude,
//                                it.longitude
//                            )
//                        )
//                    )
//                    mGoogleMap.animateCamera(
//                        CameraUpdateFactory.zoomTo(16f),
//                        3000,
//                        object : GoogleMap.CancelableCallback {
//                            override fun onCancel() {}
//                            override fun onFinish() {}
//                        }
//                    )
                    homeViewModel.shareLocationWithMyFriend(
                        friendYouWentShare!!.id!!,
                        LatLng(it.latitude,it.longitude)
                    )
                }
            }
        }
    }


    private fun observeMapToolIsOutoExtended(){
        lifecycleScope.launchWhenStarted {
            sharedViewModel.isExtendMap.collect{
                out("is extanded"+it.toString())
                if(it){
                    mGoogleMap.uiSettings.isZoomControlsEnabled = true
                    mGoogleMap.setOnCameraMoveStartedListener { reason ->
                        if (reason == OnCameraMoveStartedListener.REASON_GESTURE) {
                            binding.friendsContainer.visibility = View.GONE
                        }
                    }
                    mGoogleMap.setOnCameraIdleListener{
                        jopMapMove?.cancel()
                        jopMapMove = lifecycleScope.launch {
                            delay(1500)
                            binding.friendsContainer.visibility = View.VISIBLE
                        }
                    }
                    binding.fabMapState.visibility=View.GONE
                    isMapExtend=false
                }else{
                    mGoogleMap.uiSettings.isZoomControlsEnabled = false
                    mGoogleMap.setOnCameraMoveStartedListener(null)
                    mGoogleMap.setOnCameraIdleListener(null)
                    binding.fabMapState.visibility=View.VISIBLE
                }
            }
        }
    }

    private fun observerIsTrackingFromServices() {
        lifecycleScope.launchWhenStarted {
            TrackingService.isTracking.collect{
                if (it){
                    binding.fabStartPauseTrack.supportBackgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(requireContext(), R.color.negative))
                    binding.fabStartPauseTrack.setImageResource(R.drawable.ic_pause)
                }else{
                    binding.fabStartPauseTrack.supportBackgroundTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(requireContext(), R.color.positive))
                    binding.fabStartPauseTrack.setImageResource(R.drawable.ic_start)
                }
            }
        }
    }

    private fun observerShareLocationFromHomeViewModel() {
        lifecycleScope.launchWhenStarted {
            homeViewModel.isSaherdIt.collect{
                when{
                    it is ResultState.IsLoading ->{
                        binding.imgUpload.alpha=0.5f
                    }
                    it is ResultState.IsSucsses ->{
                        binding.imgUpload.alpha=1f
                    }
                    it is ResultState.IsError ->{
                        coustomToast(
                            requireContext(),
                            "Error ${it.message}",
                            ToastType.ERROR
                        )
                    }
                }
            }
        }
    }

    private fun observerYourFreindLocation() {
        lifecycleScope.launchWhenStarted {
            homeViewModel.friendLocation.collect{
                when{
                    it is ResultState.IsLoading ->{
                        binding.imgDownload.alpha=0.3f
                    }
                    it is ResultState.IsSucsses ->{
                        binding.imgDownload.alpha=1f
                        it.data?.let {lat->
                            friendMarker?.remove()
                            friendMarker = mGoogleMap.addMarker(
                                MarkerOptions()
                                    .position(LatLng(lat.latitude, lat.longitude))
                                    .title("my friend")
                                    .icon(bitmapDescriptorFromVector(requireContext(),R.drawable.ic_run))
                            )
                            friendLatLang.value=LatLng(lat.latitude, lat.longitude)
                        }

                    }
                    it is ResultState.IsError ->{
                        coustomToast(
                            requireContext(),
                            "Error ${it.message}",
                            ToastType.ERROR
                        )
                    }
                }
            }
        }
    }
    // --------------------------permission stuff------------------------------
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        binding.tvPermission.visibility = View.GONE
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        PermissionUtitlity.onPermissionDenied(
            this,
            perms,
            Constants.REQUEST_CODE_LOCATION,
            requireContext()
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


}