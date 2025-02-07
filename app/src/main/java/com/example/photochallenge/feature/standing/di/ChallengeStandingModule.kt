package com.example.photochallenge.feature.standing.di

import com.example.photochallenge.feature.standing.presentation.PhotoChallengeStandingViewModel
import org.koin.dsl.module

val challengeStandingModule = module{
    single { PhotoChallengeStandingViewModel(get()) }
}