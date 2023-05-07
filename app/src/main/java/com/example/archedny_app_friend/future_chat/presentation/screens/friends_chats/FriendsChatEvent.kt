package com.example.archedny_app_friend.future_chat.presentation.screens.friends_chats

import com.example.archedny_app_friend.core.domain.models.User

sealed class FriendsChatEvent{

    class OnFriendItemClick(val user: User):FriendsChatEvent()
}
