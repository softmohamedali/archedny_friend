package com.example.archedny_app_friend.data.repo

import android.app.Activity
import com.example.archedny_app_friend.data.remote.FirebaseSource
import com.example.archedny_app_friend.domain.models.User
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.PhoneAuthCredential
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ViewModelScoped
class Repo @Inject constructor(
    private val firebaseSource: FirebaseSource
) {

    fun getUsert()=firebaseSource.getUser()

    fun logOut()=firebaseSource.logOut()

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

    fun searchPhone(query:String,myPhone:String)=firebaseSource.searchPhone(query,myPhone)

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


    fun getUser(id:String)= firebaseSource.getUser(id)

    fun getMyFriends(userId: String)=firebaseSource.getMyFriend(userId)

    suspend fun getCurrentUserPhone(userId: String)=firebaseSource.getCurrentUserPhone(userId)

    suspend fun shareLocationWithMyFriend(friendId: String, latlang: LatLng) =
        withContext(Dispatchers.IO){
            firebaseSource.shareLocationWithMyFriend(friendId, latlang)
        }

    suspend fun getFriendLocation( friendId: String) = firebaseSource.getFriendLocation(friendId)



}