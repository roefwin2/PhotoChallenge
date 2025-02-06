package com.example.photochallenge.application

import android.app.Application
import com.example.photochallenge.application.di.appModule
import com.example.photochallenge.authentification.data.di.authDataModule
import com.example.photochallenge.authentification.presentation.di.authPresentationModule
import com.example.photochallenge.standing.di.challengeStandingModule
import com.example.photochallenge.takepicture.data.di.takingPictureDataModule
import com.example.photochallenge.utils.di.utilsModule
import com.example.photochallenge.voting.data.di.votingDataModule
import com.example.photochallenge.voting.presenter.di.votingPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

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
    }
}