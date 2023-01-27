package com.example.archedny_app_friend.core.data.repo

import com.example.archedny_app_friend.core.data.remote.FirebaseSource
import com.example.archedny_app_friend.core.domain.models.User
import com.example.archedny_app_friend.core.domain.repo.RepoManner
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repo constructor(
    private val firebaseSource: FirebaseSource
): RepoManner {

    override fun getUsert(): FirebaseUser?= firebaseSource.getUser()


    override fun saveUserInfo(user: User): Task<Void> =firebaseSource.saveUserInfo(user)


    override fun getUser(id: String): Task<DocumentSnapshot> = firebaseSource.getUser(id)





}