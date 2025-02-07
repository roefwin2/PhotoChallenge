package com.example.photochallenge.feature.voting.data.di

import com.example.photochallenge.feature.voting.data.PhotoChallengeVotingRepositoryImpl
import com.example.photochallenge.feature.voting.domain.PhotoChallengeVotingRepository
import org.koin.dsl.module

val votingDataModule = module {
    single<PhotoChallengeVotingRepository> {
        PhotoChallengeVotingRepositoryImpl(
            get(),
            get(),
        )
    }
}