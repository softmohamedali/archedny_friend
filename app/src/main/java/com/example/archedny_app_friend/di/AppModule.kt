package com.example.archedny_app_friend.di

import android.content.Context
import androidx.datastore.core.DataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.archedny_app_friend.utils.Constants

val android.content.Context.dataStore by preferencesDataStore(Constants.DATASTORE_MAH7LY_NAME)
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDataPref(
        @ApplicationContext context:Context
    ):DataStore<Preferences> {
        return context.dataStore
    }
}