package com.example.photochallenge.takepicture.domain

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

interface PhotoChallengeTakePictureRepository {
    fun saveCurrentPicture(takenImagePath: String): Flow<Result<Unit>>
}