package com.example.photochallenge.feature.authentification.data.di

import com.example.photochallenge.feature.authentification.data.PhotoChallengeAuthRepositoryImpl
import com.example.photochallenge.feature.authentification.domain.PhotoChallengeAuthRepository
import org.koin.dsl.module

val authDataModule = module {
    single<PhotoChallengeAuthRepository> { PhotoChallengeAuthRepositoryImpl(get(), get(), get()) }
}