package com.example.photochallenge.feature.takepicture.data.di

import com.example.photochallenge.feature.takepicture.data.PhotoChallengeTakePictureRepositoryImpl
import com.example.photochallenge.feature.takepicture.domain.PhotoChallengeTakePictureRepository
import org.koin.dsl.module

val takingPictureDataModule = module {
    single<PhotoChallengeTakePictureRepository> {
        PhotoChallengeTakePictureRepositoryImpl(
            get(),
            get()
        )
    }

}