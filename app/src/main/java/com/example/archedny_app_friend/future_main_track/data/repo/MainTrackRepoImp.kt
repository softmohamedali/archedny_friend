package com.example.archedny_app_friend.future_main_track.data.repo

import com.example.archedny_app_friend.future_main_track.data.remote.MainTrackFirebaseSource
import com.example.archedny_app_friend.future_main_track.domain.repo.MainTrackRepo
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainTrackRepoImp (
        private val mainTrackFirebaseSource: MainTrackFirebaseSource
        ):MainTrackRepo{

    override fun getUsert(): FirebaseUser?= mainTrackFirebaseSource.getUser()

    override fun searchPhone(query: String, myPhone: String): Task<QuerySnapshot> =
        mainTrackFirebaseSource.searchPhone(query,myPhone)

    override fun createTrackingChanel(
        friendId: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        mainTrackFirebaseSource.createChannel(
            friendId =friendId,
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

    override fun getMyFriends(userId: String): CollectionReference =
        mainTrackFirebaseSource.getMyFriendTracking(userId)

    override suspend fun getCurrentUserPhone(userId: String): String? =
        mainTrackFirebaseSource.getCurrentUserPhone(userId)

    override suspend fun shareLocationWithMyFriend(friendId: String, latlang: LatLng): Task<Void> =
        withContext(Dispatchers.IO){
            mainTrackFirebaseSource.shareLocationWithMyFriend(friendId, latlang)
        }

    override suspend fun getFriendLocation(friendId: String): DocumentReference =
        mainTrackFirebaseSource.getFriendLocation(friendId)



}