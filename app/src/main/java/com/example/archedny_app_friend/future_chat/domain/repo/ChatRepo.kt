package com.example.archedny_app_friend.future_chat.domain.repo

import com.example.archedny_app_friend.core.domain.models.User
import com.example.archedny_app_friend.future_chat.domain.models.TextMassage

interface ChatRepo {
    fun createChatChannel(
        friendId: String,
        onSuccess: () -> Unit,
        onError: (error: String) -> Unit,
    )

    fun sendTextMassage(
        massage: TextMassage,
        onSuccess: () -> Unit,
        onError: (error: String) -> Unit
    )

    fun getMyFriendChats(
        onSuccess: (friends:MutableList<User>) -> Unit,
        onError: (error: String) -> Unit
    )
}