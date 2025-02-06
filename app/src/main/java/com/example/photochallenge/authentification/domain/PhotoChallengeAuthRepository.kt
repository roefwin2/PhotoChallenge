package com.example.photochallenge.authentification.domain

import kotlinx.coroutines.flow.Flow

interface PhotoChallengeAuthRepository {
    fun createUser(
        lastname: String,
        email: String,
        password: String
    ): Flow<Result<Unit>>

    fun login(email: String, password: String): Flow<Result<Unit>>
    fun logout(): Result<Unit>
}