package com.example.photochallenge.feature.authentification.presentation.di

import com.example.photochallenge.feature.authentification.presentation.login.PhotoChallengeAuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(::PhotoChallengeAuthViewModel)
}