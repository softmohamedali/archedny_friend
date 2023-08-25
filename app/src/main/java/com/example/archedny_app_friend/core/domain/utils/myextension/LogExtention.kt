package com.example.archedny_app_friend.core.domain.utils.myextension

import android.util.Log


fun mylog(msg:String, cla:Any=""){
    Log.d("moali","class name:${cla.javaClass.simpleName}==>"+msg)
}