package com.example.archedny_app_friend.presentation.body.screens

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.archedny_app_friend.R
import com.example.archedny_app_friend.databinding.FragmentHomeMapBinding
import com.example.archedny_app_friend.services.TrackingService
import com.example.archedny_app_friend.utils.Constants
import com.example.archedny_app_friend.utils.PermissionUtitlity
import com.example.archedny_app_friend.utils.myextention.toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import pub.devrel.easypermissions.EasyPermissions


class HomeMapFragment : Fragment(),EasyPermissions.PermissionCallbacks {
    private var _binding: FragmentHomeMapBinding?=null
    private val binding get() = _binding!!

    var jopMapMove:Job?=null

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        googleMap.uiSettings.isZoomControlsEnabled = true
//        googleMap.animateCamera(
//            CameraUpdateFactory.zoomTo(22f),
//            3000,
//            object :GoogleMap.CancelableCallback{
//                override fun onCancel() {}
//                override fun onFinish() {}
//            }
//        )

        googleMap.setOnCameraMoveStartedListener(OnCameraMoveStartedListener { reason ->
            if (reason == OnCameraMoveStartedListener.REASON_GESTURE) {
                binding.friendsContainer.visibility=View.GONE
            }
        })
        googleMap.setOnCameraIdleListener(OnCameraIdleListener {
            jopMapMove?.cancel()
            jopMapMove=lifecycleScope.launch {
                delay(1500)
                binding.friendsContainer.visibility=View.VISIBLE
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
        _binding= FragmentHomeMapBinding.inflate(layoutInflater)
        setUp()
        return binding.root
    }

    private fun setUp() {
        if (PermissionUtitlity.hasLocationPermissin(requireContext())){
            binding.tvPermission.visibility=View.GONE
        }else{
            binding.tvPermission.visibility=View.VISIBLE
        }

        PermissionUtitlity.requestLocationPermission(
            context = requireContext(),
            fragment = this,
            requestCode = Constants.REQUEST_CODE_LOCATION,
        )
        setUpView()
        setUpViewAction()
        setUpObservers()
    }

    private fun setUpView() {

    }
    private fun setUpViewAction() {

    }

    private fun setUpObservers() {

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


    fun sendActionToService(action:String){
        Intent(requireContext(),TrackingService::class.java).also {
            it.action=action
            requireContext().startService(it)
        }
    }

    // --------------------------permission stuff------------------------------
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        binding.tvPermission.visibility=View.GONE
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
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

}