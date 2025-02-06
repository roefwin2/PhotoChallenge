package com.example.photochallenge.takepicture.data

import com.example.photochallenge.authentification.data.local.dao.PhotoChallengeUserDao
import com.example.photochallenge.authentification.domain.PhotoChallengeAuthRepository
import com.example.photochallenge.takepicture.domain.PhotoChallengeTakePictureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PhotoChallengeTakePictureRepositoryImpl(
    private val dao: PhotoChallengeUserDao,
    private val authRepository: PhotoChallengeAuthRepository
) : PhotoChallengeTakePictureRepository {
    override fun saveCurrentPicture(takenImagePath: String): Flow<Result<Unit>> = flow {
        val currentUserId = authRepository.currentUserId
            ?: throw IllegalStateException("Aucun utilisateur connecté")
        try {
            dao.updateCurrentPicture(currentUserId, takenImagePath)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}