package com.example.archedny_app_friend.future_chat.domain.models

data class Friend(
    val name:String="",
    val isActive:Boolean=false,
    val imagePath:String="",
    val lastActive:String="",
    val lastMassage:String="",
    val chatChannelId:String=""
)
