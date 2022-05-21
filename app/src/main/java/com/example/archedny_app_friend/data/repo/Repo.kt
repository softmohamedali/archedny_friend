package com.example.archedny_app_friend.data.repo

import android.app.Activity
import com.example.archedny_app_friend.data.remote.FirebaseSource
import com.example.archedny_app_friend.domain.models.User
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class Repo @Inject constructor(
    private val firebaseSource: FirebaseSource
) {

    fun getUsert()=firebaseSource.getUser()

    suspend fun registerWithPhone(
        phone:String,
        activity: Activity,
        onVerificationCompleted:()->Unit,
        onVerificationFailed:(String)->Unit,
        onCodeSent:(String)->Unit,
    ){
        firebaseSource.registerWithPhone(
            phone,
            activity,
            onVerificationCompleted,
            onVerificationFailed,
            onCodeSent
        )
    }

    fun getCredintial(
        code:String,
        verificationId:String
    )= firebaseSource.getCredintial(code,verificationId)

    fun singInWithCredential(credential: PhoneAuthCredential)=
        firebaseSource.singInWithCredential(credential)

    fun saveUserInfo(user: User)=firebaseSource.saveUserInfo(user)

    fun searchPhone(query:String)=firebaseSource.searchPhone(query)

    fun createChatChaneel(
        freindId:String,
        onSuccess:()->Unit,
        onError:(String)->Unit
    ){
        firebaseSource.createChannel(
            friendId =freindId,
            onSucess ={
                onSuccess()
            },
            onError1 = {
                onError(it)
            },
            onError2 = {
                onError(it)
            }
        )
    }


    fun getUser2(id:String)= firebaseSource.getUser2(id)

    fun getMyFriends()=firebaseSource.getMyFriend()

    suspend fun shareLocationWithMyFriend(friendId: String, latlang: LatLng) =
        withContext(Dispatchers.IO){
            firebaseSource.shareLocationWithMyFriend(friendId, latlang)
        }

    suspend fun getFriendLocation( friendId: String) =
        withContext(Dispatchers.IO){
            firebaseSource.getFriendLocation(friendId)
        }


}