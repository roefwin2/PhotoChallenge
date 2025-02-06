package com.example.photochallenge.authentification.presentation.di

import com.example.photochallenge.authentification.presentation.login.PhotoChallengeAuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authPresentationModule = module {
    viewModelOf(::PhotoChallengeAuthViewModel)
}