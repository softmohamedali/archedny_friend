package com.example.archedny_app_friend.future_chat.presentation.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archedny_app_friend.core.domain.models.User
import com.example.archedny_app_friend.core.domain.utils.validation.ResultState
import com.example.archedny_app_friend.future_chat.domain.models.TextMassage
import com.example.archedny_app_friend.future_chat.domain.usecases.GetChatContentUseCase
import com.example.archedny_app_friend.future_chat.domain.usecases.SendTextMassageUseCase
import com.example.core.domain.qulifier.IODispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatContentUseCase: GetChatContentUseCase,
    private val sendTextMassageUseCase: SendTextMassageUseCase,
    @IODispatchers
    private val ioDispatchers: CoroutineDispatcher,
): ViewModel(){

    private var _isSendTheMessage= MutableStateFlow<ResultState<String>>(ResultState.Init)
    val isSendTheMessage=_isSendTheMessage.asStateFlow()
    init {
        getChatContent()
    }


    private fun getChatContent() {
        viewModelScope.launch (ioDispatchers){

        }
    }

    fun sendMessage(textMessage:TextMassage){
        viewModelScope.launch {
            sendTextMassageUseCase(
                currentUserId= textMessage.senderId,
                TextMassage=textMessage,
                onEmitResult={
                   _isSendTheMessage.value=it
                }
            )
        }

    }




}