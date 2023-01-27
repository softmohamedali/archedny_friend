package com.example.archedny_app_friend.core.domain.utils.common

import android.content.Context
import androidx.fragment.app.Fragment
import pub.devrel.easypermissions.EasyPermissions

object PermissionManger {

    const val REQUEST_CODE_SMS_RECIVE=10

    fun hasSmsRecivePermission(context: Context)=
        EasyPermissions.hasPermissions(context,android.Manifest.permission.RECEIVE_SMS)

    fun requestSmsRecivePermission(context:Fragment){
        EasyPermissions.requestPermissions(
            context,
            "you need this permission read Otp code",
            REQUEST_CODE_SMS_RECIVE,
            android.Manifest.permission.RECEIVE_SMS
        )
    }
}