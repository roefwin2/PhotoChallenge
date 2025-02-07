package com.example.photochallenge.feature.authentification.domain

import com.example.photochallenge.feature.authentification.domain.models.PhotoChallengeUser
import kotlinx.coroutines.flow.Flow

interface PhotoChallengeAuthRepository {
   val  currentUserId: String?
    fun createUser(
        lastname: String,
        email: String,
        password: String
    ): Flow<Result<Unit>>

    fun login(email: String, password: String): Flow<Result<Unit>>
    fun initMockUsers(imageSet: Set<Int>) :Flow<Result<Unit>>
    fun getUsers(): Flow<Result<List<PhotoChallengeUser>>>
    fun logout(): Result<Unit>
}