package com.example.photochallenge.feature.voting.data

import com.example.photochallenge.feature.authentification.data.local.dao.PhotoChallengeUserDao
import com.example.photochallenge.feature.authentification.domain.PhotoChallengeAuthRepository
import com.example.photochallenge.feature.voting.domain.PhotoChallengeVotingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PhotoChallengeVotingRepositoryImpl(
    private val userDao: PhotoChallengeUserDao,
    private val authRepository: PhotoChallengeAuthRepository,
) : PhotoChallengeVotingRepository {

    override fun voteForPhoto(add: Int, photoIndex: Int): Flow<Result<Unit>> = flow {
        try {
            val currentUserId = authRepository.currentUserId
                ?: throw IllegalStateException("Aucun utilisateur connecté")

            val remainingVotes = userDao.getRemainingVotes(currentUserId)
                ?: throw IllegalStateException("Utilisateur introuvable")

            // User can vote
            if (add > 0 && remainingVotes <= 0) {
                throw IllegalStateException("Plus de votes disponibles")
            }

            val users = userDao.getAllUsers()
            if (photoIndex >= users.size) {
                throw IndexOutOfBoundsException("Index de photo invalide")
            }

            val targetUser = users[photoIndex]

            // increment/decrease vote count of the user/picture
            userDao.incrementVoteCount(targetUser.id, add)

            // Avoid negative values for votes
            if (targetUser.votingCount <= 0 && add < 0) {
                return@flow
            }

            userDao.updateRemainingVotes(currentUserId, -add)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}