package com.example.archedny_app_friend.core.domain.repo

import com.example.archedny_app_friend.core.domain.models.User
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface RepoManner {
    fun getUsert():FirebaseUser?

    fun saveUserInfo(user: User): Task<Void>


    fun getUser(id:String): Task<DocumentSnapshot>




}