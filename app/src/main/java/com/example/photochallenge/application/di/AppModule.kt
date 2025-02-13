package com.example.photochallenge.application.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.example.photochallenge.activity.MainViewModel
import com.example.photochallenge.feature.authentification.data.local.dao.PhotoChallengeUserDao
import com.example.photochallenge.feature.authentification.data.local.database.PhotoChallengeDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { WorkManager.getInstance(get()) }
    single { provideDatabase(androidContext()) }
    single<PhotoChallengeUserDao> { get<PhotoChallengeDatabase>().userDao() }
    viewModelOf(::MainViewModel)
}

private fun provideDatabase(context: Context): PhotoChallengeDatabase {
    return Room.databaseBuilder(
        context,
        PhotoChallengeDatabase::class.java,
        "service_app_database"
    ).fallbackToDestructiveMigration().build()
}