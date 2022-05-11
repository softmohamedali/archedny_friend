package com.example.archedny_app_friend.data.repo

import android.app.Activity
import com.example.archedny_app_friend.data.remote.FirebaseSource
import com.example.archedny_app_friend.domain.models.User
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.scopes.ViewModelScoped
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

}