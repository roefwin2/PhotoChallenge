package com.example.photochallenge.voting.data.di

import com.example.photochallenge.voting.data.PhotoChallengeVotingRepositoryImpl
import com.example.photochallenge.voting.domain.PhotoChallengeVotingRepository
import org.koin.dsl.module

val votingDataModule = module {
    single<PhotoChallengeVotingRepository> {
        PhotoChallengeVotingRepositoryImpl(
            get(),
            get(),
            get(),
            get()
        )
    }
}