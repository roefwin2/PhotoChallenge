package com.example.photochallenge.feature.authentification.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photochallenge.feature.authentification.domain.PhotoChallengeAuthRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PhotoChallengeAuthViewModel(
    private val authRepository: PhotoChallengeAuthRepository
) : ViewModel() {
    var authState by mutableStateOf<AuthState>(AuthState.Initial)
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            authState = AuthState.Loading
            authRepository.login(email, password).collectLatest {
                result -> result.onSuccess { authState = AuthState.Success }
                .onFailure { authState = AuthState.Error(it.message ?: "Login failed") }
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            authState = AuthState.Loading
            authRepository.createUser(lastname = name, email, password).collect{
                result -> result.onSuccess { authState = AuthState.Success }
                .onFailure { authState = AuthState.Error(it.message ?: "Registration failed") }
            }
        }
    }
}

sealed class AuthState {
    data object Initial : AuthState()
    data object Loading : AuthState()
    data object Success : AuthState()
    data class Error(val message: String) : AuthState()
}