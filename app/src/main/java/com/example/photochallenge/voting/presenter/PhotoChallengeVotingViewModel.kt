package com.example.photochallenge.voting.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photochallenge.users.domain.models.PhotoChallengePicture
import com.example.photochallenge.voting.domain.PhotoChallengeVotingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhotoChallengeVotingViewModel(
    private val votingRepository: PhotoChallengeVotingRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(VotingState())
    val state: StateFlow<VotingState> = _state.asStateFlow()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            votingRepository.getUsers().collectLatest { result ->
                result.onSuccess { users ->
                    _state.update { currentState ->
                        currentState.copy(
                            photos = users.map { it.currentPictureUri!! },
                            remainingVotes = users.firstOrNull()?.remainingVotes ?: 0
                        )
                    }
                }
            }
        }
    }

    fun voteForPhoto(add: Int, photoIndex: Int) {
        viewModelScope.launch {
            votingRepository.voteForPhoto(add, photoIndex).collectLatest { result ->
                result.onSuccess {}
            }
        }
        fetchUsers()
    }

    fun setCurrentPage(page: Int) {
        _state.update { it.copy(currentPage = page) }
    }
}

data class VotingState(
    val photos: List<PhotoChallengePicture> = emptyList(),
    val remainingVotes: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentPage: Int = 0
)