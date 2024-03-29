package com.example.archedny_app_friend.future_main_track.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archedny_app_friend.core.domain.models.User
import com.example.archedny_app_friend.core.domain.utils.validation.DataManeger
import com.example.archedny_app_friend.core.domain.utils.validation.ResultState
import com.example.archedny_app_friend.future_chat.domain.usecases.CreateChatChannelUseCase
import com.example.archedny_app_friend.future_main_track.domain.repo.MainTrackRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearshViewModel @Inject constructor(
    private val mainRepo: MainTrackRepo,
    private val createChatChannelUseCase: CreateChatChannelUseCase,
):ViewModel() {

    private var _phones= MutableStateFlow<ResultState<List<User>>>(ResultState.Init)
    var phones:StateFlow<ResultState<List<User>>> =_phones
    fun searchPhone(phone:String){
        viewModelScope.launch(Dispatchers.IO) {
            _phones.emit(ResultState.IsLoading)
            val userId:String=mainRepo.getUsert()?.uid!!
            mainRepo.searchPhone(phone,mainRepo.getCurrentUserPhone(userId)!!).addOnCompleteListener {
                if (it.isSuccessful){
                    _phones.value= DataManeger.handledata(it.result)
                }else{
                    _phones.value= ResultState.IsError("Error : ${it.exception?.message}")
                }

            }
        }
    }


    private var _isAddFriend= MutableStateFlow<ResultState<Boolean>>(ResultState.Init)
    var isAddFriend:StateFlow<ResultState<Boolean>> =_isAddFriend
    fun addFreind(
        freindId:String
    ){
        viewModelScope.launch (Dispatchers.IO){
            _isAddFriend.value= ResultState.IsLoading
            createChatChannelUseCase(
                friendId = freindId,
                onEmitResult = {
                    when(it){
                        is ResultState.IsLoading ->{
                            _isAddFriend.value= ResultState.IsLoading
                        }
                        is ResultState.IsSucsses ->{

                            mainRepo.createTrackingChanel(
                                friendId = freindId,
                                onSuccess = {
                                    _isAddFriend.value= ResultState.IsSucsses()
                                },
                                onError = {
                                    _isAddFriend.value= ResultState.IsError(it)
                                }
                            )
                        }
                        is ResultState.IsError ->{
                            _isAddFriend.value= ResultState.IsError(it.message!!)
                        }
                    }
                }
            )
        }
    }


}