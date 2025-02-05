package com.example.photochallenge.users.domain

import android.net.Uri
import com.example.photochallenge.users.domain.models.PhotoChallengeUser

interface PhotoChallengeRepository {
    val currentUser: PhotoChallengeUser?
    fun createUser(
        firstname: String,
        lastname: String,
        email: String,
        password: String
    ): Result<Unit>

    fun login(email: String, password: String): Result<Unit>

    fun getUsers(): Result<List<PhotoChallengeUser>>
    fun saveCurrentPicture(takenUri: Uri): Result<Unit>
    fun voteForPhoto(add: Int, photoIndex: Int): Result<Unit>
    fun logout(): Result<Unit>
}