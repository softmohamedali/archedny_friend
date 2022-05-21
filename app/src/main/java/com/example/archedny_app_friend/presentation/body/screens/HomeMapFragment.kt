package com.example.archedny_app_friend.presentation.body.screens

import android.content.Intent
import android.content.res.ColorStateList
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
import com.example.archedny_app_friend.domain.models.User
import com.example.archedny_app_friend.presentation.body.adapters.FriendItemAdapter
import com.example.archedny_app_friend.presentation.body.viewmodels.HomeViewModel
import com.example.archedny_app_friend.services.TrackingService
import com.example.archedny_app_friend.utils.Constants
import com.example.archedny_app_friend.utils.PermissionUtitlity
import com.example.archedny_app_friend.utils.ResultState
import com.example.archedny_app_friend.utils.myextention.ToastType
import com.example.archedny_app_friend.utils.myextention.coustomToast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import pub.devrel.easypermissions.EasyPermissions



@AndroidEntryPoint
class HomeMapFragment : Fragment(), EasyPermissions.PermissionCallbacks {
    private var _binding: FragmentHomeMapBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel>()
    private val friendItemAdapter by lazy { FriendItemAdapter() }
    private lateinit var mGoogleMap: GoogleMap
    private var friendYouWentShare:User?=null
    var myMarker:Marker?=null
    var friendMarker:Marker?=null

    var jopMapMove: Job? = null

    private val callback = OnMapReadyCallback { googleMap ->
        mGoogleMap = googleMap
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.setOnCameraMoveStartedListener(OnCameraMoveStartedListener { reason ->
            if (reason == OnCameraMoveStartedListener.REASON_GESTURE) {
                binding.friendsContainer.visibility = View.GONE
            }
        })
        googleMap.setOnCameraIdleListener(OnCameraIdleListener {
            jopMapMove?.cancel()
            jopMapMove = lifecycleScope.launch {
                delay(1500)
                binding.friendsContainer.visibility = View.VISIBLE
            }
        })
    }

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
        binding.fabStartPauseTrack.setOnClickListener {
            if (TrackingService.isTracking.value){
                endTracking()
            }else{
                startTracking()
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
        observerCurrentLocationFromServices()
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
                    )
                    mGoogleMap.moveCamera(
                        CameraUpdateFactory.newLatLng(
                            LatLng(
                                it.latitude,
                                it.longitude
                            )
                        )
                    )
                    mGoogleMap.animateCamera(
                        CameraUpdateFactory.zoomTo(16f),
                        3000,
                        object : GoogleMap.CancelableCallback {
                            override fun onCancel() {}
                            override fun onFinish() {}
                        }
                    )
                    homeViewModel.shareLocationWithMyFriend(
                        friendYouWentShare!!.id!!,
                        LatLng(it.latitude,it.longitude)
                    )
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
                        binding.imgDownload.alpha=0.5f
                    }
                    it is ResultState.IsSucsses ->{
                        binding.imgDownload.alpha=1f
                        it.data?.let {lat->
                            friendMarker?.remove()
                            friendMarker = mGoogleMap.addMarker(
                                MarkerOptions()
                                    .position(LatLng(lat.latitude, lat.longitude))
                                    .title("me")
                            )
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

}