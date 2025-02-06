package com.example.photochallenge.voting.presenter.di

import com.example.photochallenge.voting.presenter.PhotoChallengeVotingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val votingPresentationModule = module {
    viewModelOf(::PhotoChallengeVotingViewModel)
}