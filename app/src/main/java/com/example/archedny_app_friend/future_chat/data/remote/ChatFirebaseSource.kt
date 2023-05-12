package com.example.archedny_app_friend.future_chat.data.remote

import android.util.Log
import com.example.archedny_app_friend.core.domain.models.User
import com.example.archedny_app_friend.core.domain.utils.validation.Constants
import com.example.archedny_app_friend.future_chat.domain.models.TextMassage
import com.example.archedny_app_friend.future_chat.domain.utils.ChatConstants
import com.example.archedny_app_friend.future_chat.domain.utils.ChatConstants.COLLECTION_CHAT_CHANNELS
import com.example.archedny_app_friend.future_chat.domain.utils.ChatConstants.COLLECTION_MASSAGES_IN_CHAT_CHANNELS
import com.example.archedny_app_friend.future_chat.domain.utils.ChatConstants.PROPERTY_CHAT_CHANNELS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
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

    fun sendTextMassage(
        massage:TextMassage,
        onSuccess: () -> Unit,
        onError: (error: String) -> Unit
    ) {
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


//    private suspend fun handleGetFriends(
//        value: QuerySnapshot,
//        onSuccess: () -> Unit
//    ){
//        out("start")
//        out("1")
//        val users= mutableListOf<User>()
//
//            out("2")
//
//
//                value.documents.forEach {
//                    out("3")
//                    getUser(it.id).addOnCompleteListener {task->
//                        if (task.isSuccessful){
//                            onSuccess(task.to)
//                        }
//                    }
//                    users.add(getUser(it.id).toObject(User::class.java)!!)
//                    out("4")
//                }
//            invokeOnCompletion {
//                out("5")
//                if (users.isNotEmpty()){
//                    _users.value= ResultState.IsSucsses(users)
//                }else{
//                    _users.value= ResultState.IsError("No Data Found")
//                }
//            }
//
//    }


}