package com.example.archedny_app_friend.di

import com.example.archedny_app_friend.core.data.remote.FirebaseSource
import com.example.archedny_app_friend.core.data.repo.Repo
import com.example.archedny_app_friend.core.domain.repo.RepoManner
import com.example.archedny_app_friend.future_auth.data.remote.AuthFirebaseSource
import com.example.archedny_app_friend.future_auth.data.repo.AuthRepoImp
import com.example.archedny_app_friend.future_auth.domain.repo.AuthRepo
import com.example.archedny_app_friend.future_main_track.data.remote.MainTrackFirebaseSource
import com.example.archedny_app_friend.future_main_track.data.repo.MainTrackRepoImp
import com.example.archedny_app_friend.future_main_track.domain.repo.MainTrackRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object RepoModule {

    @Provides
    @ViewModelScoped
    fun provideRepoManner(firebaseSource: FirebaseSource): RepoManner =
        Repo(firebaseSource)

    @Provides
    @ViewModelScoped
    fun provideAuthRepo(authFirebaseSource: AuthFirebaseSource): AuthRepo =
        AuthRepoImp(authFirebaseSource)

    @Provides
    @ViewModelScoped
    fun provideMainTrackRepo(mainTrackFirebaseSource: MainTrackFirebaseSource): MainTrackRepo =
        MainTrackRepoImp(mainTrackFirebaseSource)
}