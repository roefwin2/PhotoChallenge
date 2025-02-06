package com.example.photochallenge.authentification.data.local.entity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.photochallenge.users.domain.models.PhotoChallengePicture
import com.example.photochallenge.users.domain.models.PhotoChallengeUser
import com.example.photochallenge.utils.ImageStorage
import java.io.ByteArrayOutputStream

@Entity(tableName = "challenge_users")
data class PhotoChallengeUserEntity(
    @PrimaryKey
    val id: String,
    val lastname: String,
    val email: String,
    val password: String,
    val picturePath: String?,
    val votingCount: Int = 0,
    val remainingVotes: Int = 5
)

suspend fun PhotoChallengeUserEntity.toPhotoChallengeUser(imageStorage: ImageStorage): PhotoChallengeUser {
    return PhotoChallengeUser(
        userId = id,
        firstname = lastname,
        lastname = lastname,
        email = email,
        password = password,
        currentPictureUri = PhotoChallengePicture(
            bitmap = byteArrayToBitmap(picturePath?.let { imageStorage.getImageFile(it) }),
            votingCount = votingCount
        ),
        currentScore = 100,
        remainingVotes = remainingVotes

    )
}

fun byteArrayToBitmap(byteArray: ByteArray?): Bitmap? {
    if (byteArray == null) {
        return null
    }
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream) // PNG garde la qualité, JPEG réduit la taille
    return stream.toByteArray()
}