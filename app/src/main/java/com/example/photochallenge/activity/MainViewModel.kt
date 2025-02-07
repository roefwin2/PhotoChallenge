package com.example.photochallenge.activity

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photochallenge.feature.authentification.data.MockData
import com.example.photochallenge.feature.authentification.data.local.entity.bitmapToByteArray
import com.example.photochallenge.feature.authentification.domain.PhotoChallengeAuthRepository
import com.example.photochallenge.feature.takepicture.domain.PhotoChallengeTakePictureRepository
import com.example.photochallenge.core.utils.ImageStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val photoChallengeTakePictureRepository: PhotoChallengeTakePictureRepository,
    private val authRepository: PhotoChallengeAuthRepository,
    private val imageStorage: ImageStorage
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        initNewChallenge()
    }

    private fun initNewChallenge() {
        val mockUsers = setOf(MockData.Angry, MockData.Sad)
        val selectedData = mockUsers.random()
        _state.value = state.value.copy(
            selectedEmoji = selectedData.emojis
        )
        viewModelScope.launch(Dispatchers.Default) {
            authRepository.initMockUsers(selectedData.userPictures).collectLatest {
                it.onSuccess {
                    Log.d("MainViewModel", "Mock users initialized")
                }
            }
        }
    }

    fun onTakenPhoto(bitmap: Bitmap) {
        _state.value = state.value.copy(
            bitmap = bitmap
        )
    }

    fun onSavePhoto() {
        val currentBitmap = state.value.bitmap ?: return
        viewModelScope.launch(Dispatchers.Default) {
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
            bitmap = null,
            isPaywallVisible = false
        )
    }

    fun onPremiumFeature() {
        _state.value = state.value.copy(
            isPaywallVisible = true
        )
    }
}

data class MainState(
    val bitmap: Bitmap? = null,
    val isPaywallVisible: Boolean = false,
    val selectedEmoji: Int? = null
)