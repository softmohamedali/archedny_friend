//package com.example.archedny_app_friend.di
//
//import android.content.Context
//import androidx.room.Room
//import com.example.archedny_app_friend.core.data.local.MyDao
//import com.example.archedny_app_friend.data.local.MyDataBase
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import javax.inject.Singleton
//
//
//@Module
//@InstallIn(SingletonComponent::class)
//object DataBaseModule {
//
//    @Provides
//    @Singleton
//    fun provideDatabase(
//        @ApplicationContext context: Context
//    ): MyDataBase {
//        return Room.databaseBuilder(
//            context,
//            MyDataBase::class.java,
//            "mydatabase"
//        ).build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideMyDao(
//        dataBase: MyDataBase
//    ): MyDao =dataBase.myDao()
//}