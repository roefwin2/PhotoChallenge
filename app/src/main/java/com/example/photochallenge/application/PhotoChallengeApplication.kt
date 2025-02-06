package com.example.photochallenge.application

import android.app.Application
import com.example.photochallenge.application.di.appModule
import com.example.photochallenge.authentification.data.di.authDataModule
import com.example.photochallenge.authentification.presentation.di.authPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class PhotoChallengeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PhotoChallengeApplication)
            modules(
                listOf(
                    appModule,
                    authDataModule,
                    authPresentationModule
                )
            )
        }
    }
}