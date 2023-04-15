package com.example.archedny_app_friend.future_chat.domain.usecases

import com.example.archedny_app_friend.core.domain.utils.validation.ResultState
import com.example.archedny_app_friend.future_chat.domain.repo.ChatRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class CreateChatChannelUseCase(
    private val chatRepo: ChatRepo
) {

    suspend operator fun invoke(
        friendId: String,
        onEmitResult:(ResultState<String>)->Unit
    ){
        onEmitResult(ResultState.IsLoading)
        chatRepo.createChatChannel(
            friendId=friendId,
            onError = {
                onEmitResult(ResultState.IsError(it))
            },
            onSuccess = {

                onEmitResult(ResultState.IsSucsses())
            }
        )
    }
}