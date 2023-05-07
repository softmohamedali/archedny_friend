package com.example.archedny_app_friend.future_chat.presentation.screens.friends_chats

import com.example.archedny_app_friend.core.domain.models.User

data class FriendChatsState(
    val isSuccesses:Boolean=false,
    val isLoading:Boolean=true,
    val error:String?=null,
    val myFriendsList:List<User> = listOf()
) {
}