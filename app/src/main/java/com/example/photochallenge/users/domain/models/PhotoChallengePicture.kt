package com.example.photochallenge.users.domain.models

import android.net.Uri

data class PhotoChallengePicture(
    val uri: Uri,
    val votingCount: Int = 0
)
