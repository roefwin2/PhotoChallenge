package com.example.photochallenge.feature.voting.presenter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photochallenge.feature.authentification.domain.PhotoChallengeAuthRepository
import com.example.photochallenge.feature.authentification.domain.models.PhotoChallengePicture
import com.example.photochallenge.feature.voting.domain.PhotoChallengeVotingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhotoChallengeVotingViewModel(
    private val votingRepository: PhotoChallengeVotingRepository,
    private val photoChallengeAuthRepository: PhotoChallengeAuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow(VotingState())
    val state: StateFlow<VotingState> = _state.asStateFlow()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        val currentUser = photoChallengeAuthRepository.currentUserId ?: return
        viewModelScope.launch {
            photoChallengeAuthRepository.getUsers()
                .distinctUntilChanged()
                .collectLatest { result ->
                result.onSuccess { users ->
                    _state.update { currentState ->
                        currentState.copy(
                            photos = users.map { it.currentPictureUri!! },
                            remainingVotes = users.firstOrNull { it.userId == currentUser }?.remainingVotes
                                ?: 0
                        )
                    }
                }
            }
        }
    }

    fun voteForPhoto(add: Int, photoIndex: Int) {
        viewModelScope.launch {
            votingRepository.voteForPhoto(add, photoIndex).collectLatest { result ->
                result.onSuccess {
                    //May be search a better solution to avoid the recomposition
                    fetchUsers()
                }
            }
        }
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