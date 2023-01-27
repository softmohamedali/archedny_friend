package com.example.archedny_app_friend.future_main_track.data.remote

import com.example.archedny_app_friend.core.domain.models.User
import com.example.archedny_app_friend.core.domain.utils.validation.Constants
import com.example.archedny_app_friend.future_main_track.domain.models.ChatChannel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainTrackFirebaseSource @Inject constructor(
    val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
) {
    fun getUser() = auth.currentUser

    fun getMyFriend(userId: String)= firestore.collection(Constants.USER_COLLECTION).document(userId)
        .collection("chatchennel")

    suspend fun getCurrentUserPhone(userId: String)=firestore.collection(Constants.USER_COLLECTION)
        .document(userId)
        .get()
        .await()
        .toObject(User::class.java)!!
        .phone

    fun searchPhone(query: String,myphone:String) = firestore.collection(Constants.USER_COLLECTION)
        .orderBy("phone")
        .whereNotEqualTo("phone",myphone)
        .startAt(query.trim())
        .endAt(query.trim() + "\uF8FF")
        .startAt(query.trim().uppercase(Locale.ROOT))
        .get()

    fun createChannel(
        userId: String = getUser()!!.uid,
        friendId: String,
        onSucess: () -> Unit,
        onError1: (error1: String) -> Unit,
        onError2: (error2: String) -> Unit,
    ) {
        val id = firestore.collection(Constants.USER_COLLECTION).document().id
        val result1 = firestore.collection(Constants.USER_COLLECTION).document(userId)
            .collection("chatchennel")
            .document(friendId)
            .set(mapOf("chatChannelId" to id))
            .isSuccessful
        val result2 = firestore.collection(Constants.USER_COLLECTION).document(friendId)
            .collection("chatchennel")
            .document(userId)
            .set(mapOf("chatChannelId" to id))
            .isSuccessful

        if (!result1 && !result2) {
            onSucess()
        } else {
            onError1("Error")
        }
    }

    suspend fun getFriendChatChannel(friendId: String)=
        firestore.collection(Constants.USER_COLLECTION)
            .document(getUser()!!.uid)
            .collection("chatchennel")
            .document(friendId)
            .get()
            .await()
            .toObject(ChatChannel::class.java)

    suspend fun shareLocationWithMyFriend(friendId: String, latlang: LatLng) =
        firestore.collection(Constants.CHAT_CHANNELS_COLLECTION)
            .document(getFriendChatChannel(friendId)!!.chatChannelId)
            .collection(getUser()!!.uid)
            .document(Constants.SENDER_LOCATION_DOCUMENT)
            .set(latlang)


    suspend fun getFriendLocation(friendId: String) =
        firestore.collection(Constants.CHAT_CHANNELS_COLLECTION)
            .document(getFriendChatChannel(friendId)!!.chatChannelId)
            .collection(friendId)
            .document(Constants.SENDER_LOCATION_DOCUMENT)



}