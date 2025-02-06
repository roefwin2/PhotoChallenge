package com.example.photochallenge.authentification.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenge_users")
data class PhotoChallengeUserEntity(
    @PrimaryKey
    val id :String,
    val lastname: String,
    val email: String,
    val password: String,
    val currentPictureUri: String?,
)