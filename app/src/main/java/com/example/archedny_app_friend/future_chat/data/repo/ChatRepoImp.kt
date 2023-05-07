package com.example.archedny_app_friend.future_chat.data.repo

import com.example.archedny_app_friend.core.domain.models.User
import com.example.archedny_app_friend.future_chat.data.remote.ChatFirebaseSource
import com.example.archedny_app_friend.future_chat.domain.models.TextMassage
import com.example.archedny_app_friend.future_chat.domain.repo.ChatRepo

class ChatRepoImp(
    private val chatFirebaseSource: ChatFirebaseSource
):ChatRepo {
    override fun createChatChannel(
        friendId: String,
        onSuccess: () -> Unit,
        onError: (error: String) -> Unit
    ) {
        chatFirebaseSource.createChatChannel(
           friendId, onSuccess, onError
        )
    }

    override fun sendTextMassage(
        chatChanelId: String,
        massage: TextMassage,
        onSuccess: () -> Unit,
        onError: (error: String) -> Unit
    ) {
        chatFirebaseSource.sendTextMassage(
            chatChanelId, massage, onSuccess, onError
        )
    }

    override fun getMyFriendChats(onSuccess: (friends:MutableList<User>) -> Unit, onError: (error: String) -> Unit) {
        chatFirebaseSource.getMyFriendChats(onSuccess = onSuccess,onError=onError)
    }
}