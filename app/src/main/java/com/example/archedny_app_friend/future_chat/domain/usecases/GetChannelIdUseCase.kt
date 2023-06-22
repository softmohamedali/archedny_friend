package com.example.archedny_app_friend.future_chat.domain.usecases

import com.example.archedny_app_friend.core.domain.utils.validation.ResultState
import com.example.archedny_app_friend.future_chat.domain.models.TextMassage
import com.example.archedny_app_friend.future_chat.domain.repo.ChatRepo

class GetChannelIdUseCase(
    private val chatRepo: ChatRepo
) {

    operator fun invoke(
        friendId:String,
        onEmitResult:(ResultState<String>)->Unit
    ){
        onEmitResult(ResultState.IsLoading)
        chatRepo.getChatChannelId(
            friendId = friendId,
            onError = {
                onEmitResult(ResultState.IsError(it))
            },
            onSuccess = {
                onEmitResult(ResultState.IsSucsses(it))
            }
        )
    }
}