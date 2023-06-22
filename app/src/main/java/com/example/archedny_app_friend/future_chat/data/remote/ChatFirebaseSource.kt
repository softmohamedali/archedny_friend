package com.example.archedny_app_friend.future_chat.data.remote

import android.util.Log
import com.example.archedny_app_friend.core.domain.models.User
import com.example.archedny_app_friend.core.domain.utils.validation.Constants
import com.example.archedny_app_friend.future_chat.domain.models.TextMassage
import com.example.archedny_app_friend.future_chat.domain.utils.ChatConstants
import com.example.archedny_app_friend.future_chat.domain.utils.ChatConstants.COLLECTION_CHAT_CHANNELS
import com.example.archedny_app_friend.future_chat.domain.utils.ChatConstants.COLLECTION_MASSAGES_IN_CHAT_CHANNELS
import com.example.archedny_app_friend.future_chat.domain.utils.ChatConstants.PROPERTY_CHAT_CHANNELS
import com.example.archedny_app_friend.future_main_track.domain.models.ChatChannel
import com.example.archedny_app_friend.future_main_track.domain.models.TrackChannel
import com.example.archedny_app_friend.future_main_track.domain.utils.MainTrackConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ChatFirebaseSource @Inject constructor(
    val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val dataStorage: FirebaseStorage,
){
    fun getUser() = auth.currentUser

    fun getUser(
        id: String,
        onSuccess: (user:User) -> Unit,
        onError: (error: String) -> Unit,
    ) {
        firestore.collection(Constants.USER_COLLECTION)
            .document(id)
            .get()
            .addOnCompleteListener {task->
                if (task.isSuccessful){
                    onSuccess(task.result.toObject(User::class.java)!!)
                }else{
                    onError("Error : ${task.exception?.message}")
                }
            }
    }

    fun createChatChannel(
        friendId: String,
        onSuccess: () -> Unit,
        onError: (error: String) -> Unit,
    ) {
        val chatChanelId = firestore.collection(Constants.USER_COLLECTION).document().id
        firestore.collection(Constants.USER_COLLECTION).document( getUser()!!.uid)
            .collection(COLLECTION_CHAT_CHANNELS)
            .document(friendId)
            .set(mapOf(PROPERTY_CHAT_CHANNELS to chatChanelId))
            .addOnCompleteListener {
                if (it.isSuccessful){
                    firestore.collection(Constants.USER_COLLECTION).document(friendId)
                        .collection(COLLECTION_CHAT_CHANNELS)
                        .document( getUser()!!.uid)
                        .set(mapOf(PROPERTY_CHAT_CHANNELS to chatChanelId))
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                onSuccess()
                            }else{
                                onError("Error : ${it.exception?.message}")
                            }
                        }
                }else{
                    onError("Error : ${it.exception?.message}")
                }
            }
    }


    fun getChatChannel(
        friendId: String,
        onSuccess: (chatChannelId:String) -> Unit,
        onError: (error: String) -> Unit
    ){
        firestore.collection(Constants.USER_COLLECTION)
            .document(getUser()!!.uid)
            .collection(COLLECTION_CHAT_CHANNELS)
            .document(friendId)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    Log.d("moali in firebasesouce","getchatchannelid${it.result.toObject(ChatChannel::class.java)!!.chatChannelsId}")
                    onSuccess(it.result.toObject(ChatChannel::class.java)!!.chatChannelsId)
                }else{
                    onError("Error : ${it.exception?.message}")
                }
            }
    }

    fun sendTextMassage(
        massage:TextMassage,
        onSuccess: () -> Unit,
        onError: (error: String) -> Unit
    ) {
        Log.d("moali in firebasesouce","chatchannelid${massage.chatChanneId}")
        if(massage.msg.isEmpty()){
            firestore.collection(COLLECTION_CHAT_CHANNELS).document(massage.chatChanneId)
                .collection(COLLECTION_MASSAGES_IN_CHAT_CHANNELS)
                .document().set(massage)
            return
        }
        massage.senderId = getUser()?.uid
        val massageDocument =
            firestore.collection(COLLECTION_CHAT_CHANNELS).document(massage.chatChanneId)
                .collection(COLLECTION_MASSAGES_IN_CHAT_CHANNELS)
                .document()
        massage.id = massageDocument.id
        massageDocument.set(massage).addOnCompleteListener {
            if (it.isSuccessful) {
                onSuccess()
            } else {
                onError("Error : ${it.exception?.message}")
            }
        }
    }


    fun getMyFriendChats(
        onSuccess: (friends:MutableList<User>) -> Unit,
        onError: (error: String) -> Unit
    ){
        val myFriends= mutableListOf<User>()
        firestore.collection(Constants.USER_COLLECTION)
            .document(getUser()!!.uid)
            .collection(ChatConstants.COLLECTION_CHAT_CHANNELS)
            .addSnapshotListener { value, error ->
                if (error==null){
                    value?.documents?.forEach {
                        getUser(
                            id = it.id,
                            onSuccess = {friend->
                                myFriends.add(friend)
                                onSuccess(myFriends)
                            },
                            onError={error->
                                onError(error)
                            }
                        )
                    }
                    //TODO try to do that in better way onSuccess(myFriends)
                    Log.d("moali ChatFirebaseSource" ,"size after ${myFriends.size}")
                }else{
                    onError(error.message!!)
                }
            }
    }


    fun getMessagesChatChannelContent(
        chatChannelId:String,
        onSuccess: (messages:MutableList<TextMassage>) -> Unit,
        onError: (error: String) -> Unit,
    ){
        val myMessages= mutableListOf<TextMassage>()
        firestore.collection(COLLECTION_CHAT_CHANNELS).document(chatChannelId)
            .collection(COLLECTION_MASSAGES_IN_CHAT_CHANNELS)
            .orderBy("date",Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                myMessages.clear()
                if (error==null&&value!=null){
                    value.documents.forEach {doc->
                        myMessages.add(doc.toObject(TextMassage::class.java)!!)
                    }
                    onSuccess(myMessages)
                }else{
                    onError(error?.message!!)
                }
            }

    }





}