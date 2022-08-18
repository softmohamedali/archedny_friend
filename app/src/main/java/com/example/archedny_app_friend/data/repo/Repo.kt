package com.example.archedny_app_friend.data.repo

import android.app.Activity
import com.example.archedny_app_friend.data.remote.FirebaseSource
import com.example.archedny_app_friend.domain.models.User
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class Repo constructor(
    private val firebaseSource: FirebaseSource
): RepoManner {

    override fun getUsert(): FirebaseUser?= firebaseSource.getUser()

    override fun logOut()=firebaseSource.logOut()

    override suspend fun registerWithPhone(
        phone: String,
        activity: Activity,
        onVerificationCompleted: () -> Unit,
        onVerificationFailed: (String) -> Unit,
        onCodeSent: (String) -> Unit
    ) {
        firebaseSource.registerWithPhone(
            phone,
            activity,
            onVerificationCompleted,
            onVerificationFailed,
            onCodeSent
        )
    }

    override fun getCredintial(code: String, verificationId: String): PhoneAuthCredential=
        firebaseSource.getCredintial(code,verificationId)

    override fun singInWithCredential(credential: PhoneAuthCredential): Task<AuthResult> =
        firebaseSource.singInWithCredential(credential)

    override fun saveUserInfo(user: User): Task<Void> =firebaseSource.saveUserInfo(user)

    override fun searchPhone(query: String, myPhone: String): Task<QuerySnapshot> =
        firebaseSource.searchPhone(query,myPhone)

    override fun createChatChaneel(
        freindId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
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

    override fun getUser(id: String): Task<DocumentSnapshot> = firebaseSource.getUser(id)

    override fun getMyFriends(userId: String): CollectionReference =
        firebaseSource.getMyFriend(userId)

    override suspend fun getCurrentUserPhone(userId: String): String? =
        firebaseSource.getCurrentUserPhone(userId)

    override suspend fun shareLocationWithMyFriend(friendId: String, latlang: LatLng): Task<Void>  =
        withContext(Dispatchers.IO){
            firebaseSource.shareLocationWithMyFriend(friendId, latlang)
        }

    override suspend fun getFriendLocation(friendId: String): DocumentReference =
        firebaseSource.getFriendLocation(friendId)





}