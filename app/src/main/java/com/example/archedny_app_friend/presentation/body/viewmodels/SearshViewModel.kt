package com.example.archedny_app_friend.presentation.body.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archedny_app_friend.data.repo.Repo
import com.example.archedny_app_friend.domain.models.User
import com.example.archedny_app_friend.utils.DataManeger
import com.example.archedny_app_friend.utils.ResultState
import com.example.archedny_app_friend.utils.myextention.toast
import com.example.archedny_app_friend.utils.out
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearshViewModel @Inject constructor(
    private val repo: Repo
):ViewModel() {

    private var _phones= MutableStateFlow<ResultState<List<User>>>(ResultState.Init)
    var phones:StateFlow<ResultState<List<User>>> =_phones
    fun searchPhone(phone:String){
        viewModelScope.launch(Dispatchers.IO) {
            _phones.emit(ResultState.IsLoading)
            val userId:String=repo.getUsert()?.uid!!
            repo.searchPhone(phone,repo.getCurrentUserPhone(userId)!!).addOnCompleteListener {
                if (it.isSuccessful){
                    _phones.value=DataManeger.handledata(it.result)
                }else{
                    _phones.value=ResultState.IsError("Error : ${it.exception?.message}")
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
            _isAddFriend.value=ResultState.IsLoading
            repo.createChatChaneel(
                freindId = freindId,
                onSuccess = {
                    _isAddFriend.value=ResultState.IsSucsses()
                },
                onError = {
                    _isAddFriend.value=ResultState.IsError(it)
                }
            )
        }
    }


}