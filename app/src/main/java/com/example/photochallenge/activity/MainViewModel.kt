package com.example.photochallenge.activity

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photochallenge.authentification.data.local.entity.bitmapToByteArray
import com.example.photochallenge.takepicture.domain.PhotoChallengeTakePictureRepository
import com.example.photochallenge.utils.ImageStorage
import com.example.photochallenge.voting.data.MockData
import com.example.photochallenge.voting.domain.PhotoChallengeVotingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val photoChallengeTakePictureRepository: PhotoChallengeTakePictureRepository,
    private val photoChallengeVotingRepository: PhotoChallengeVotingRepository,
    private val imageStorage: ImageStorage
) : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        val mockUsers = setOf(MockData.Angry,MockData.Sad)
        val selectedData = mockUsers.random()
        _state.value = state.value.copy(
            selectedEmoji = selectedData.emojis
        )
        viewModelScope.launch(Dispatchers.Default) {
            photoChallengeVotingRepository.initMockUsers(selectedData.userPictures)
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