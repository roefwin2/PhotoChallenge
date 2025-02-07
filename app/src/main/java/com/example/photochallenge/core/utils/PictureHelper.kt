package com.example.photochallenge.core.utils

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID


class ImageStorage(
    private val context: Context
) {
    suspend fun saveImage(image: ByteArray): String {
        return withContext(Dispatchers.Default) {
            val filename = "${UUID.randomUUID()}.png"
            context.openFileOutput(filename, Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write(image)
            }
            filename
        }
    }

    suspend fun getImageFile(filename: String): ByteArray {
        return withContext(Dispatchers.Default) {
            context.openFileInput(filename).use { inputStream ->
                inputStream.readBytes()
            }
        }
    }
}

