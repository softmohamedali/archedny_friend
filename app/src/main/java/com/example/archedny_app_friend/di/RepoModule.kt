package com.example.archedny_app_friend.di

import com.example.archedny_app_friend.data.remote.FirebaseSource
import com.example.archedny_app_friend.data.repo.Repo
import com.example.archedny_app_friend.data.repo.RepoManner
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(ViewModelComponent::class)
object RepoModule {

    @Provides
    @ViewModelScoped
    fun provideRepoManner(firebaseSource: FirebaseSource): RepoManner =
        Repo(firebaseSource)


}