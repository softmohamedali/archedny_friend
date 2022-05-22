package com.example.archedny_app_friend.data.remote

import android.app.Activity
import com.example.archedny_app_friend.domain.models.ChatChannel
import com.example.archedny_app_friend.domain.models.User
import com.example.archedny_app_friend.utils.Constants
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseSource @Inject constructor(
    val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val dataStorage: FirebaseStorage,
    private val googleSingInClient: GoogleSignInClient,
    private val phoneAuth: PhoneAuthProvider
) {

    fun getUser() = auth.currentUser

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

    fun saveUserInfo(user: User): Task<Void> {
        val ref = firestore.collection(Constants.USER_COLLECTION)
        val mUser = user
        mUser.id = getUser()?.uid
        return ref.document(mUser.id!!).set(user)
    }

    fun getUser2(id: String) =
        firestore.collection(Constants.USER_COLLECTION).document(id).get()


    fun getMyFriend() = firestore.collection(Constants.USER_COLLECTION).document(getUser()!!.uid)
        .collection("chatchennel")


    fun searchPhone(query: String) = firestore.collection(Constants.USER_COLLECTION)
        .orderBy("phone")
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
            .document(Constants.RECIVER_LOCATION_DOCUMENT)
            .get()


}


//
//
//// ----------------------------------------Authentication-------------------------------------
//fun user() = auth.currentUser
//
//fun createUsre(email: String, password: String) =
//    auth.createUserWithEmailAndPassword(email, password)
//
//fun login(email: String, password: String) =
//    auth.signInWithEmailAndPassword(email, password)
//
//fun loginGoogle() = googleSingInClient.signInIntent
//
//fun firebaseSinginWithGoogle(idToken: String): Task<AuthResult> {
//    val credential = GoogleAuthProvider.getCredential(idToken, null)
//    return auth.signInWithCredential(credential)
//}
//
//fun logout() = auth.signOut()
//
//fun resetPassword(email: String) = auth.sendPasswordResetEmail(email)
//
//fun changePassword(currePass: String, newPass: String): Task<Void> {
//    val creditinal = EmailAuthProvider.getCredential(user()?.email!!, currePass)
//    return user()?.reauthenticate(creditinal)!!
//}
////-----------------------------------------------------------------------------------------
//
//
////----------------------------------------FireStore----------------------------------------
//
////    fun saveUser(client: Client): Task<Void> {
////        val ref = firestore.collection(Constants.COLLLECTION_USERS)
////            .document(auth.currentUser!!.uid)
////        var mclient = client
////        mclient.clientId = ref.id
////        return ref.set(mclient)
////    }
//
//
//fun getDoctors() = firestore.collection(Constants.COLLLECTION_DOCTORS_ACCEPTED)
//
//fun getDoctorById(doctorId: String) =
//    firestore.collection(Constants.COLLLECTION_DOCTORS_ACCEPTED)
//        .document(doctorId).get()
//
//fun getAllDoctorsTimeSchedula(doctorId: String) =
//    firestore.collection(Constants.TIME_SCHEDULA_COLLECTION)
//        .document(doctorId).collection("date")
//        .orderBy("date", Query.Direction.DESCENDING)
//
//fun uploadMyAppointement(appointeiment: Appointeiment): Task<Void> {
//    val ref = firestore.collection(Constants.USER_APPOINTEMENT_COLLECTION).document()
//    val appo = appointeiment
//    appo.appointeimentId = ref.id
//    return ref.set(appo)
//}
//
//
//fun getSpeiality() =
//    firestore.collection(Constants.COLLLECTION_SPEIALITY)
//
//fun getSearchSpeiality(query: String) =
//    firestore.collection(Constants.COLLLECTION_SPEIALITY)
//        .orderBy("name")
//        .startAt(query.trim())
//        .endAt(query.trim() + "\uF8FF")
//        .startAt(query.trim().toUpperCase())
//
//
//fun getDoctorsWithSpetilst(spetilist: String) =
//    firestore.collection(Constants.COLLLECTION_DOCTORS_ACCEPTED)
//        .whereEqualTo(NAME_FIELD_SPETIALTY, spetilist)
//
//fun getDoctorSearchByName(name: String) =
//    firestore.collection(Constants.COLLLECTION_DOCTORS_ACCEPTED)
//        .orderBy(NAME_FIELD_NAMEEN)
//        .startAt(name.trim())
//        .endAt(name.trim() + "\uF8FF")
//
//fun upLoadMassage() = firestore.collection("massages")
//    .document()
//
//fun getUserInfo() = firestore.collection(Constants.COLLLECTION_USERS)
//    .document(auth.currentUser!!.uid).get()
//
//fun getUserApponitement() = firestore.collection(Constants.USER_APPOINTEMENT_COLLECTION)
//    .whereEqualTo("clientId", auth.currentUser?.uid)
//
//fun delteAppointement(appoId: String) =
//    firestore.collection(Constants.USER_APPOINTEMENT_COLLECTION)
//        .document(appoId).delete()
//
//fun addTimeSchedula(time: TimeSchedule, doctorId: String): Task<Void> {
//    val ref = firestore.collection(Constants.TIME_SCHEDULA_COLLECTION)
//        .document(doctorId).collection("date").document()
//    val mytime = time
//    mytime.id = ref.id
//    return ref.set(mytime)
//}
//
//fun deleteTimeSchedula(timeSchId: String, doctorId: String) =
//    firestore.collection(Constants.TIME_SCHEDULA_COLLECTION)
//        .document(doctorId).collection("date").document(timeSchId).delete()
//
//fun getLapTests() = firestore.collection(Constants.COLLLECTION_LAPTESTS)
//
//fun getCoviedTests() = firestore.collection(Constants.COLLLECTION_COVIEDTESTS)
//
//fun getSearchLapTests(name: String) = firestore.collection(Constants.COLLLECTION_LAPTESTS)
//    .orderBy("name")
//    .startAt(name.trim())
//    .endAt(name.trim() + "\uf8ff")
//
//fun bookLapTests(lapTestsBook: BookTest) =
//    firestore.collection(Constants.COLLECTION_BOOKINGlAPTESTS)
//
//fun getBranches(area: String, city: String) =
//    firestore.collection(Constants.COLLLECTION_BRANCHES)
//
//fun getAllBranches() = firestore.collection(Constants.COLLLECTION_BRANCHES)
//
//
//fun addTestForUser(test: MedicalTest): Task<Void> {
//    if (test.id.isNullOrEmpty()) {
//        val ref = firestore.collection(Constants.COLLLECTION_MEDICALTEST).document()
//        val mytest = test
//        mytest.id = ref.id
//        mytest.userid = user()?.uid
//        return ref.set(mytest)
//    } else {
//        return firestore.collection(Constants.COLLLECTION_MEDICALTEST).document(test.id!!)
//            .set(test)
//    }
//}
//
//fun deleteMedicalTestForUser(medicalTest: MedicalTest) =
//    firestore.collection(Constants.COLLLECTION_MEDICALTEST).document(medicalTest.id!!)
//        .delete()
//
//fun deleteMedicationForUser(medication: Medecation) =
//    firestore.collection(Constants.COLLLECTION_MEDICATION).document(medication.id!!)
//        .delete()
//
//fun addMedicationForUser(meidication: Medecation): Task<Void> {
//    if (meidication.id.isNullOrEmpty()) {
//        val ref = firestore.collection(Constants.COLLLECTION_MEDICATION).document()
//        val myMedication = meidication
//        myMedication.id = ref.id
//        myMedication.userid = user()?.uid
//        return ref.set(myMedication)
//    } else {
//        return firestore.collection(Constants.COLLLECTION_MEDICALTEST)
//            .document(meidication.id!!).set(meidication)
//    }
//
//}
//
//fun getUserMedicalTest() = firestore.collection(Constants.COLLLECTION_MEDICALTEST)
//    .whereEqualTo("userid", user()?.uid)
//
//fun getUserMedication() = firestore.collection(Constants.COLLLECTION_MEDICATION)
//    .whereEqualTo("userid", user()?.uid)
//
////-------------------------------------------------------------------------------------------
//
//
////----------------------------------------DataStorage----------------------------------------
//
//fun saveImg(imgpitmap: ByteArray, onSuccesUpload: (String) -> Unit) {
//    val ref = dataStorage.reference
//        .child("${auth.currentUser?.uid}/images/${UUID.randomUUID()}${Random.nextInt()}")
//    val task = ref.putBytes(imgpitmap)
//    var downloadUrl: String? = "soak"
//    runBlocking {
//        task.continueWithTask {
//            if (!it.isSuccessful) {
//                it.exception?.let {
//                    throw it
//                }
//            }
//            ref.downloadUrl
//        }.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                downloadUrl = task.result.toString()
//                onSuccesUpload(downloadUrl!!)
//            }
//        }
//    }
//}
//
//
////-------------------------------------------------------------------------------------------