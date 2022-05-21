

package com.example.archedny_app_friend.presentation.body.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archedny_app_friend.data.repo.Repo
import com.example.archedny_app_friend.domain.models.User
import com.example.archedny_app_friend.utils.ResultState
import com.example.archedny_app_friend.utils.out
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: Repo
) : ViewModel() {

    private var _users = MutableStateFlow<ResultState<MutableList<User>>>(ResultState.Init)
    var users: StateFlow<ResultState<MutableList<User>>> = _users

    fun getMyFreiends() {
        viewModelScope.launch(Dispatchers.IO) {
            _users.emit(ResultState.IsLoading)
            repo.getMyFriends().addSnapshotListener { value, error ->
                if (error == null) {
                    handleGetFriends(value!!)
                } else {
                    _users.value = ResultState.IsError(error.message!!)
                }
            }

        }
    }

    private fun handleGetFriends(value:QuerySnapshot){
        out("start")
        out("1")
        val users= mutableListOf<User>()
        viewModelScope.launch(Dispatchers.IO) {
            out("2")
            async {
                value.documents.forEach {
                    out("3")
                    users.add(getUser(it.id).toObject(User::class.java)!!)
                    out("4")
                }
            }.invokeOnCompletion {
                out("5")
                if (users.isNotEmpty()){
                    _users.value=ResultState.IsSucsses(users)
                }else{
                    _users.value=ResultState.IsError("No Data Found")
                }
            }
        }
    }

    suspend fun getUser(id: String )=repo.getUser2(id).await()

    private var _isSaherdIt = MutableStateFlow<ResultState<Boolean>>(ResultState.Init)
    var isSaherdIt: StateFlow<ResultState<Boolean>> = _isSaherdIt

    fun shareLocationWithMyFriend(friendId: String, latlang: LatLng) {
        viewModelScope.launch (Dispatchers.IO){
            _isSaherdIt.emit(ResultState.IsLoading)
            val result=repo.shareLocationWithMyFriend(friendId,latlang)
            if (!result.isSuccessful){
                _isSaherdIt.emit(ResultState.IsSucsses())
            }else{
                _isSaherdIt.emit(ResultState.IsError(result.exception?.message?:"unkown error"))
            }
        }
    }

    private var _friendLocation = MutableStateFlow<ResultState<LatLng>>(ResultState.Init)
    var friendLocation: StateFlow<ResultState<LatLng>> = _friendLocation

    fun getFriendLocation(friendId: String) {
        viewModelScope.launch (Dispatchers.IO){
            _friendLocation.emit(ResultState.IsLoading)
            repo.getFriendLocation(friendId).addOnCompleteListener{
                if (!it.isSuccessful){
                    _friendLocation.value=ResultState.IsSucsses(it.result.toObject(LatLng::class.java)!!)
                }else{
                    _friendLocation.value=ResultState.IsError(it.exception?.message?:"")
                }
            }

        }
    }

}