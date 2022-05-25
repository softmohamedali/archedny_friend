

package com.example.archedny_app_friend.presentation.body.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archedny_app_friend.data.repo.Repo
import com.example.archedny_app_friend.domain.models.MyLatLang
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
            if (repo.getUsert()?.uid!=null){
                repo.getMyFriends(repo.getUsert()?.uid!!).addSnapshotListener { value, error ->
                    if (error == null) {
                        handleGetFriends(value!!)
                    } else {
                        _users.value = ResultState.IsError(error.message!!)
                    }
                }
            }else{
                _users.value = ResultState.IsError("please login to continue")
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

    suspend fun getUser(id: String )=repo.getUser(id).await()

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

    private var _friendLocation = MutableStateFlow<ResultState<MyLatLang>>(ResultState.Init)
    var friendLocation: StateFlow<ResultState<MyLatLang>> = _friendLocation

    fun getFriendLocation(friendId: String) {
        viewModelScope.launch (Dispatchers.IO){
            _friendLocation.emit(ResultState.IsLoading)
            repo.getFriendLocation(friendId).addSnapshotListener { value, error ->
                if (error==null){
                    if (value!=null){
                        _friendLocation.value=ResultState.IsSucsses(
                            value.toObject(MyLatLang::class.java)?:MyLatLang()
                        )
                    }else{
                        _friendLocation.value=ResultState.IsError("please ask your friend to share his location")
                    }
                }else{
                    _friendLocation.value=ResultState.IsError(error.message?:"")
                }
            }

        }
    }

}