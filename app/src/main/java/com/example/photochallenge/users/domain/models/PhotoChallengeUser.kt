package com.example.photochallenge.users.domain.models

data class PhotoChallengeUser(
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
    val currentPictureUri: PhotoChallengePicture?,
    val currentScore: Int,
    val remainingVotes: Int
)
