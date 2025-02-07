package com.example.photochallenge.application

import android.app.Application
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.photochallenge.application.di.appModule
import com.example.photochallenge.core.utils.di.utilsModule
import com.example.photochallenge.feature.authentification.data.di.authDataModule
import com.example.photochallenge.feature.authentification.presentation.di.authPresentationModule
import com.example.photochallenge.feature.standing.di.challengeStandingModule
import com.example.photochallenge.feature.takepicture.data.di.takingPictureDataModule
import com.example.photochallenge.feature.voting.data.di.votingDataModule
import com.example.photochallenge.feature.voting.presenter.di.votingPresentationModule
import com.example.photochallenge.feature.workmanager.NotificationWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.concurrent.TimeUnit

class PhotoChallengeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PhotoChallengeApplication)
            modules(
                listOf(
                    appModule,
                    authDataModule,
                    authPresentationModule,
                    takingPictureDataModule,
                    votingDataModule,
                    votingPresentationModule,
                    challengeStandingModule,
                    utilsModule,
                )
            )
        }

        // WorkManager configuration
        WorkManager.initialize(
            this,
            Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.INFO)
                .build()
        )
    }
}