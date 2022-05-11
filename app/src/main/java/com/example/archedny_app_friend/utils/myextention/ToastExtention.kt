package com.example.archedny_app_friend.utils.myextention

import android.content.Context
import android.widget.Toast

fun toast(
    context: Context,
    msg:String?="toast here"
){
    Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
}