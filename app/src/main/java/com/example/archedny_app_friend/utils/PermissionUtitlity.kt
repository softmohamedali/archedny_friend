package com.example.archedny_app_friend.utils

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

object PermissionUtitlity {

    //try this agin with the if condition just for courijus
    fun hasLocationPermissin(context:Context):Boolean=
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.Q)
        {
            EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        }else {
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            )
        }


    fun requestLocationPermission(
        context: Context,
        fragment: Fragment,
        msgIfDeniedRequest:String="you need to accepte this permission to have utily of these app",
        requestCode:Int,
    ){
        if (hasLocationPermissin(context)){
            return
        }
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.Q)
        {
            EasyPermissions.requestPermissions(
                fragment,
                msgIfDeniedRequest,
                requestCode,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        }else {
            EasyPermissions.requestPermissions(
                fragment,
                msgIfDeniedRequest,
                requestCode,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            )
        }
    }

    fun onPermissionDenied(
        fragment: Fragment,
        perems:MutableList<String>,
        requestCode:Int,
        context: Context
    ){
        if (EasyPermissions.somePermissionPermanentlyDenied(fragment,perems)){
            AppSettingsDialog.Builder(fragment).build().show()
        }else{
            requestLocationPermission(
                fragment = fragment,
                context = context,
                requestCode = requestCode
            )
        }
    }
}