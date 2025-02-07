package com.example.photochallenge.authentification.data.di

import com.example.photochallenge.authentification.data.PhotoChallengeAuthRepositoryImpl
import com.example.photochallenge.authentification.domain.PhotoChallengeAuthRepository
import org.koin.dsl.module

val authDataModule = module {
    single<PhotoChallengeAuthRepository> { PhotoChallengeAuthRepositoryImpl(get(), get(), get()) }
}