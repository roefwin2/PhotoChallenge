package com.example.photochallenge.users.domain.models

import android.graphics.Bitmap

data class PhotoChallengePicture(
    val bitmap: Bitmap?,
    val votingCount: Int = 0
)
