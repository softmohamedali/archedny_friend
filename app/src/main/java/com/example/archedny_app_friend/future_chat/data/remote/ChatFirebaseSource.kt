package com.example.archedny_app_friend.future_chat.data.remote

import com.example.archedny_app_friend.core.domain.utils.validation.Constants
import com.example.archedny_app_friend.future_chat.domain.models.TextMassage
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
        chatChanelId:String,
        massage:TextMassage,
        onSuccess: () -> Unit,
        onError: (error: String) -> Unit
    ){
        val massageDocument=firestore.collection(COLLECTION_CHAT_CHANNELS).document(chatChanelId)
            .collection(COLLECTION_MASSAGES_IN_CHAT_CHANNELS)
            .document()
        massage.id=massageDocument.id
        massageDocument.set(massage).addOnCompleteListener {
            if (it.isSuccessful){
                onSuccess()
            }else{
                onError("Error : ${it.exception?.message}")
            }
        }
    }



}