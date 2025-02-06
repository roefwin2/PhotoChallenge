package com.example.photochallenge.activity

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    fun onTakenPhoto(bitmap: Bitmap) {
        _state.value = state.value.copy(
            bitmap = bitmap
        )
    }

    fun onSavePhoto() {

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