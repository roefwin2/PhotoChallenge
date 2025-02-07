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
            // Récupérer l'ID de l'utilisateur courant
            val currentUserId = authRepository.currentUserId
                ?: throw IllegalStateException("Aucun utilisateur connecté")

            // Vérifier les votes restants
            val remainingVotes = userDao.getRemainingVotes(currentUserId)
                ?: throw IllegalStateException("Utilisateur introuvable")

            // Vérifier si l'utilisateur peut voter
            if (add > 0 && remainingVotes <= 0) {
                throw IllegalStateException("Plus de votes disponibles")
            }

            // Récupérer tous les utilisateurs pour trouver celui à l'index donné
            val users = userDao.getAllUsers()
            if (photoIndex >= users.size) {
                throw IndexOutOfBoundsException("Index de photo invalide")
            }

            // Récupérer l'utilisateur cible
            val targetUser = users[photoIndex]

            // Effectuer les mises à jour dans une transaction
            userDao.incrementVoteCount(targetUser.id, add)

            // Mettre à jour le nombre de votes restants de l'utilisateur courant
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