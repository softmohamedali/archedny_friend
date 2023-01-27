package com.example.archedny_app_friend.future_auth.data.repo

import android.app.Activity
import com.example.archedny_app_friend.core.data.remote.FirebaseSource
import com.example.archedny_app_friend.future_auth.data.remote.AuthFirebaseSource
import com.example.archedny_app_friend.future_auth.domain.repo.AuthRepo
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.PhoneAuthCredential
import javax.inject.Inject
import javax.inject.Singleton


class AuthRepoImp(
    private val authFirebaseSource: AuthFirebaseSource
):AuthRepo {

    override fun logOut()=authFirebaseSource.logOut()

    override suspend fun registerWithPhone(
        phone: String,
        activity: Activity,
        onVerificationCompleted: () -> Unit,
        onVerificationFailed: (String) -> Unit,
        onCodeSent: (String) -> Unit
    ) {
        authFirebaseSource.registerWithPhone(
            phone,
            activity,
            onVerificationCompleted,
            onVerificationFailed,
            onCodeSent
        )
    }

    override fun getCredintial(code: String, verificationId: String): PhoneAuthCredential =
        authFirebaseSource.getCredintial(code,verificationId)

    override fun singInWithCredential(credential: PhoneAuthCredential): Task<AuthResult> =
        authFirebaseSource.singInWithCredential(credential)
}