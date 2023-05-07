package com.example.archedny_app_friend.core.domain.models

data class User(
    var id:String?="",
    var phone:String?="",
    var name:String?="",
    var createAt:String?="",
    val isActive:Boolean=false,
    val imagePath:String="",
    val lastActive:String="",
)
