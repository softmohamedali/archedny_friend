package com.example.archedny_app_friend.di

import com.example.core.domain.qulifier.IODispatchers
import com.example.core.domain.qulifier.MainDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


@InstallIn(ViewModelComponent::class)
@Module
object DispatchersModule {

    @IODispatchers
    @Provides
    @ViewModelScoped
    fun provideIoDispatchers():CoroutineDispatcher=Dispatchers.IO


    @MainDispatchers
    @Provides
    @ViewModelScoped
    fun provideMainDispatchers():CoroutineDispatcher=Dispatchers.Main



}