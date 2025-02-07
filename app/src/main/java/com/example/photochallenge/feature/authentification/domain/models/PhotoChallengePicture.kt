package com.example.photochallenge.feature.authentification.domain.models

import android.graphics.Bitmap

data class PhotoChallengePicture(
    val bitmap: Bitmap?,
    val votingCount: Int = 0
)
