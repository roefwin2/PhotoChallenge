package com.example.photochallenge.standing.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photochallenge.authentification.domain.PhotoChallengeAuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class PhotoChallengeStandingViewModel(
    authRepository: PhotoChallengeAuthRepository,
) : ViewModel() {
    val standingState = authRepository.getUsers().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Result.success(emptyList())
    )
}