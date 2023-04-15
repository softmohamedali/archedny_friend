package com.example.archedny_app_friend.future_chat.domain.repo

import com.example.archedny_app_friend.future_chat.domain.models.TextMassage

interface ChatRepo {
    fun createChatChannel(
        friendId: String,
        onSuccess: () -> Unit,
        onError: (error: String) -> Unit,
    )

    fun sendTextMassage(
        chatChanelId:String,
        massage: TextMassage,
        onSuccess: () -> Unit,
        onError: (error: String) -> Unit
    )
}