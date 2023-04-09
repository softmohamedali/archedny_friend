package com.example.archedny_app_friend.future_chat.domain.models

data class TextMassage(
    val msg:String,
    val date:String,
    val isRead:Boolean=false,
    val itReceived:Boolean=false,
    var id:String
)
