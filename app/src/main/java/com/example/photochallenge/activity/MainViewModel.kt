package com.example.photochallenge.activity

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photochallenge.authentification.data.local.entity.bitmapToByteArray
import com.example.photochallenge.takepicture.domain.PhotoChallengeTakePictureRepository
import com.example.photochallenge.utils.ImageStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val photoChallengeTakePictureRepository: PhotoChallengeTakePictureRepository,
    private val imageStorage: ImageStorage
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    fun onTakenPhoto(bitmap: Bitmap) {
        _state.value = state.value.copy(
            bitmap = bitmap
        )
    }

    fun onSavePhoto() {
        val currentBitmap = state.value.bitmap ?: return
        viewModelScope.launch {
            val currentImagePath = imageStorage.saveImage(bitmapToByteArray(currentBitmap))
            photoChallengeTakePictureRepository.saveCurrentPicture(currentImagePath).collect {
                it.onSuccess {
                    onDeletedPhoto()
                }
            }
        }
    }

    fun onDeletedPhoto() {
        _state.value = state.value.copy(
            bitmap = null
        )
    }
}

data class MainState(
    val bitmap: Bitmap? = null
)