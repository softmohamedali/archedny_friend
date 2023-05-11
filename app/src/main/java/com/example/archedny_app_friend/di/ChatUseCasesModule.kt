package com.example.archedny_app_friend.di

import com.example.archedny_app_friend.core.data.remote.FirebaseSource
import com.example.archedny_app_friend.core.data.repo.Repo
import com.example.archedny_app_friend.core.domain.repo.RepoManner
import com.example.archedny_app_friend.future_chat.domain.repo.ChatRepo
import com.example.archedny_app_friend.future_chat.domain.usecases.CreateChatChannelUseCase
import com.example.archedny_app_friend.future_chat.domain.usecases.GetChatContentUseCase
import com.example.archedny_app_friend.future_chat.domain.usecases.GetFriendChatsUseCase
import com.example.archedny_app_friend.future_chat.domain.usecases.SendTextMassageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ChatUseCasesModule {

    @Provides
    @ViewModelScoped
    fun provideCreateChatChannelUseCase(chatRepo: ChatRepo): CreateChatChannelUseCase =
        CreateChatChannelUseCase(chatRepo)

    @Provides
    @ViewModelScoped
    fun provideSendTextMassageUseCase(chatRepo: ChatRepo): SendTextMassageUseCase =
        SendTextMassageUseCase(chatRepo)

    @Provides
    @ViewModelScoped
    fun provideGetFriendsChatsMassageUseCase(chatRepo: ChatRepo): GetFriendChatsUseCase=
        GetFriendChatsUseCase(chatRepo)

    @Provides
    @ViewModelScoped
    fun provideGetChatContentUseCase(chatRepo: ChatRepo): GetChatContentUseCase=
        GetChatContentUseCase(chatRepo)

}

