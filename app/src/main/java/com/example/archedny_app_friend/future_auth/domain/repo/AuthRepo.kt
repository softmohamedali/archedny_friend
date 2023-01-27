package com.example.archedny_app_friend.future_auth.domain.repo

import android.app.Activity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential

interface AuthRepo {

    fun logOut()

    suspend fun registerWithPhone(
        phone:String,
        activity: Activity,
        onVerificationCompleted:()->Unit,
        onVerificationFailed:(String)->Unit,
        onCodeSent:(String)->Unit,
    )

    fun getCredintial(
        code:String,
        verificationId:String
    ): PhoneAuthCredential

    fun singInWithCredential(credential: PhoneAuthCredential): Task<AuthResult>
}