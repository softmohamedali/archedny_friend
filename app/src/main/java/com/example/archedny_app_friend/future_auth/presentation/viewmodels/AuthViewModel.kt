package com.example.archedny_app_friend.future_auth.presentation.viewmodels

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.archedny_app_friend.core.domain.repo.RepoManner
import com.example.archedny_app_friend.core.domain.models.User
import com.example.archedny_app_friend.core.domain.utils.validation.ResultState
import com.example.archedny_app_friend.future_auth.domain.repo.AuthRepo
import com.google.firebase.auth.PhoneAuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private  val repo: RepoManner,
    private val authRepo: AuthRepo
):ViewModel() {

    fun getUser()=repo.getUsert()

    fun logOut()=authRepo.logOut()

    private var _isVerifiying= MutableStateFlow<ResultState<String>>(ResultState.Init)
    val isVerifiying:StateFlow<ResultState<String>> get() = _isVerifiying

    fun registerVerifiyWithPhone(
        phone:String,
        activity: Activity
    ){
        viewModelScope.launch(Dispatchers.IO){
            _isVerifiying.emit(ResultState.IsLoading)
            authRepo.registerWithPhone(
                phone,
                activity,
                onVerificationCompleted = {
                },
                onVerificationFailed = { message ->
                    _isVerifiying.value= ResultState.IsError(message)
                },
                onCodeSent = {verificationId ->
                    _isVerifiying.value= ResultState.IsSucsses(verificationId)
                }
            )
        }
    }

    fun getCredintial(
        code:String,
        verificationId:String
    )=authRepo.getCredintial(code,verificationId)

    private var _isSingIn= MutableStateFlow<ResultState<String>>(ResultState.Init)
    val isSingIn:StateFlow<ResultState<String>> = _isSingIn
    fun singInWithCredential(credential: PhoneAuthCredential){
        viewModelScope.launch(Dispatchers.IO) {
            _isSingIn.emit(ResultState.IsLoading)
            authRepo.singInWithCredential(credential = credential).addOnCompleteListener {
                if (it.isSuccessful){
                    _isSingIn.value= ResultState.IsSucsses()
                }else{
                    _isSingIn.value= ResultState.IsError(it.exception?.message!!)
                }
            }
        }
    }

    private var _isSaveUser= MutableStateFlow<ResultState<String>>(ResultState.Init)
    val isSaveUser:StateFlow<ResultState<String>> = _isSaveUser
    fun saveUser(user: User){
        viewModelScope.launch (Dispatchers.IO){
            _isSaveUser.emit(ResultState.IsLoading)
            repo.saveUserInfo(user).addOnCompleteListener {
                if (it.isSuccessful){
                    _isSaveUser.value= ResultState.IsSucsses()
                }else{
                    _isSaveUser.value= ResultState.IsError(it.exception?.message!!)
                }
            }
        }
    }



}