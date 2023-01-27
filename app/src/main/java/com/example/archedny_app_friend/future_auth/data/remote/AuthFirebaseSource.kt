package com.example.archedny_app_friend.future_auth.data.remote

import android.app.Activity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthFirebaseSource @Inject constructor(
    val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val dataStorage: FirebaseStorage,
    private val googleSingInClient: GoogleSignInClient,
    private val phoneAuth: PhoneAuthProvider
    ){

    fun logOut()=auth.signOut()

    fun registerWithPhone(
        phone: String,
        activity: Activity,
        onVerificationCompleted: () -> Unit,
        onVerificationFailed: (String) -> Unit,
        onCodeSent: (String) -> Unit,
    ) {
        auth.getFirebaseAuthSettings().setAppVerificationDisabledForTesting(true)
        phoneAuth.verifyPhoneNumber(
            phone,
            55L,
            TimeUnit.SECONDS,
            activity,
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                    onVerificationCompleted()
                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    onVerificationFailed(p0.message!!)
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)
                    onCodeSent(p0)
                }
            }
        )
    }

    fun getCredintial(
        code: String,
        verificationId: String
    ) = PhoneAuthProvider.getCredential(verificationId, code)

    fun singInWithCredential(credential: PhoneAuthCredential) =
        auth.signInWithCredential(credential)



}