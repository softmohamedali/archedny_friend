package com.example.archedny_app_friend.future_main_track.domain.repo

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface MainTrackRepo {
    fun getUsert(): FirebaseUser?
    fun searchPhone(query:String,myPhone:String): Task<QuerySnapshot>

    fun createChatChaneel(
        freindId:String,
        onSuccess:()->Unit,
        onError:(String)->Unit
    )


    fun getMyFriends(userId: String): CollectionReference

    suspend fun getCurrentUserPhone(userId: String):String?

    suspend fun shareLocationWithMyFriend(friendId: String, latlang: LatLng) : Task<Void>

    suspend fun getFriendLocation( friendId: String): DocumentReference

}