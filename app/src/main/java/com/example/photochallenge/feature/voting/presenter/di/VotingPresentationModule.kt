package com.example.photochallenge.feature.voting.presenter.di

import com.example.photochallenge.feature.voting.presenter.PhotoChallengeVotingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val votingPresentationModule = module {
    viewModelOf(::PhotoChallengeVotingViewModel)
}