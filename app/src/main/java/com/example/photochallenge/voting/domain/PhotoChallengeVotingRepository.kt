package com.example.photochallenge.voting.domain

import com.example.photochallenge.users.domain.models.PhotoChallengeUser
import kotlinx.coroutines.flow.Flow

interface PhotoChallengeVotingRepository {
    fun initMockUsers(imageSet: Set<Int>)
    fun voteForPhoto(add: Int, photoIndex: Int): Flow<Result<Unit>>
    fun getUsers(): Flow<Result<List<PhotoChallengeUser>>>
}