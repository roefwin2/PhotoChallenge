package com.example.photochallenge.feature.voting.domain

import kotlinx.coroutines.flow.Flow

interface PhotoChallengeVotingRepository {
    fun voteForPhoto(add: Int, photoIndex: Int): Flow<Result<Unit>>

}