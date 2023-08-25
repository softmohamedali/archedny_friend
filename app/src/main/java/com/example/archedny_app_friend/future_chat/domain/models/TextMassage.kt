package com.example.archedny_app_friend.future_chat.domain.models


data class TextMassage(
    var msg:String="",
    var date:String="",
    var isRead:Boolean=false,
    var itReceived:Boolean=false,
    var id:String?=null,
    var senderId:String?=null,
    var receiverId:String="",
    var chatChanneId:String="",
)
