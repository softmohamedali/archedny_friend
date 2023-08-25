package com.example.archedny_app_friend.future_chat.domain.usecases

import com.example.archedny_app_friend.core.domain.models.User
import com.example.archedny_app_friend.core.domain.utils.myextension.mylog
import com.example.archedny_app_friend.core.domain.utils.validation.ResultState
import com.example.archedny_app_friend.future_chat.domain.models.TextMassage
import com.example.archedny_app_friend.future_chat.domain.repo.ChatRepo

class GetChatContentUseCase (
    private val chatRepo: ChatRepo
) {

    suspend operator fun invoke(
        chatChannelId:String,
        onEmitResult:(ResultState<List<TextMassage>>)->Unit
    ){
        onEmitResult(ResultState.IsLoading)
        chatRepo.getMessagesChatChannelContent(
            chatChannelId = chatChannelId,
            onError = {
                onEmitResult(ResultState.IsError(it))
            },
            onSuccess = {
                mylog("my massages size${it.size}",this)
                onEmitResult(ResultState.IsSucsses(it))
            }
        )
    }
}