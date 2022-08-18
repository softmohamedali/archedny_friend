package com.example.archedny_app_friend.data.repo

import android.app.Activity
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

interface RepoManner {
    fun getUsert():FirebaseUser?

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
    ):PhoneAuthCredential

    fun singInWithCredential(credential: PhoneAuthCredential): Task<AuthResult>

    fun saveUserInfo(user: User): Task<Void>

    fun searchPhone(query:String,myPhone:String): Task<QuerySnapshot>

    fun createChatChaneel(
        freindId:String,
        onSuccess:()->Unit,
        onError:(String)->Unit
    )


    fun getUser(id:String): Task<DocumentSnapshot>

    fun getMyFriends(userId: String): CollectionReference

    suspend fun getCurrentUserPhone(userId: String):String?

    suspend fun shareLocationWithMyFriend(friendId: String, latlang: LatLng) : Task<Void>

    suspend fun getFriendLocation( friendId: String): DocumentReference


}