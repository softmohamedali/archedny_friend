package com.example.archedny_app_friend.future_chat.presentation.screens.friends_chats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archedny_app_friend.core.domain.models.User
import com.example.archedny_app_friend.core.domain.utils.validation.ResultState
import com.example.archedny_app_friend.future_chat.domain.usecases.GetFriendChatsUseCase
import com.example.core.domain.qulifier.IODispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class FriendChatsViewModel @Inject constructor(
    private val getFriendChatsUseCase: GetFriendChatsUseCase,
    @IODispatchers
    private val ioDispatchers: CoroutineDispatcher,
):ViewModel(){

    private var _myFriendChats= MutableStateFlow<ResultState<List<User>>>(ResultState.Init)
    val myFriendChats=_myFriendChats.asStateFlow()
    init {
        getFriendChats()
    }


    private fun getFriendChats() {
        viewModelScope.launch (ioDispatchers){
            getFriendChatsUseCase(
                onEmitResult = {result->
                    _myFriendChats.value=result
                }
            )
        }

    }




}