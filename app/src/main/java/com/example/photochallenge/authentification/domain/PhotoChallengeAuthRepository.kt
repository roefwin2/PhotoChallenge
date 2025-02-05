package com.example.photochallenge.authentification.domain

interface PhotoChallengeAuthRepository {
    fun createUser(
        firstname: String,
        lastname: String,
        email: String,
        password: String
    ): Result<Unit>

    fun login(email: String, password: String): Result<Unit>
    fun logout(): Result<Unit>
}