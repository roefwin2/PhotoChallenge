package com.example.photochallenge.standing.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photochallenge.voting.domain.PhotoChallengeVotingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class PhotoChallengeStandingViewModel(
    photoChallengeVotingRepository: PhotoChallengeVotingRepository
) : ViewModel() {
    val standingState = photoChallengeVotingRepository.getUsers().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Result.success(emptyList())
    )
}