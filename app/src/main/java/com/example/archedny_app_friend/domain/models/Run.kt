package com.example.archedny_app_friend.domain.models

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

data class Run(
    var id:Int?=null,
    var img: Bitmap?=null,
    var timeStart:Long?=0L,
    var avgSpeed:Float?=0F,
    var distence:Int?=0,
    var runTime:Long?=0L,
    var caloryBurned:Int?=0
){

}