package com.example.photochallenge.takepicture.data.di

import com.example.photochallenge.takepicture.data.PhotoChallengeTakePictureRepositoryImpl
import com.example.photochallenge.takepicture.domain.PhotoChallengeTakePictureRepository
import org.koin.dsl.module

val takingPictureDataModule = module {
    single<PhotoChallengeTakePictureRepository> {
        PhotoChallengeTakePictureRepositoryImpl(
            get(),
            get()
        )
    }

}