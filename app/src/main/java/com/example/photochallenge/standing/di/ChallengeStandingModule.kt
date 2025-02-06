package com.example.photochallenge.standing.di

import com.example.photochallenge.standing.presentation.PhotoChallengeStandingViewModel
import org.koin.dsl.module

val challengeStandingModule = module{
    single { PhotoChallengeStandingViewModel(get()) }
}