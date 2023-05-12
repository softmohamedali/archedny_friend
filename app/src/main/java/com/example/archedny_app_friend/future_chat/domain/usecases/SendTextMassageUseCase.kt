package com.example.archedny_app_friend.future_chat.domain.usecases

import com.example.archedny_app_friend.core.domain.utils.validation.ResultState
import com.example.archedny_app_friend.future_chat.domain.models.TextMassage
import com.example.archedny_app_friend.future_chat.domain.repo.ChatRepo

class SendTextMassageUseCase(
    private val chatRepo: ChatRepo
) {

    suspend operator fun invoke(
        TextMassage: TextMassage,
        onEmitResult:(ResultState<String>)->Unit
    ){
        onEmitResult(ResultState.IsLoading)
        chatRepo.sendTextMassage(
            TextMassage,
            onError = {
                onEmitResult(ResultState.IsError(it))
            },
            onSuccess = {

                onEmitResult(ResultState.IsSucsses())
            }
        )
    }
}