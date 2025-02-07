package com.example.photochallenge.voting.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.photochallenge.R
import com.example.photochallenge.authentification.data.local.dao.PhotoChallengeUserDao
import com.example.photochallenge.authentification.data.local.entity.PhotoChallengeUserEntity
import com.example.photochallenge.authentification.data.local.entity.toPhotoChallengeUser
import com.example.photochallenge.authentification.domain.PhotoChallengeAuthRepository
import com.example.photochallenge.users.domain.models.PhotoChallengeUser
import com.example.photochallenge.utils.ImageStorage
import com.example.photochallenge.voting.domain.PhotoChallengeVotingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PhotoChallengeVotingRepositoryImpl(
    private val userDao: PhotoChallengeUserDao,
    private val authRepository: PhotoChallengeAuthRepository,
    private val context: Context,
    private val imageStorage: ImageStorage
) : PhotoChallengeVotingRepository {

    override fun initMockUsers(imageSet: Set<Int>) {
        val mockUsers = imageSet.map {
            val imagePah = copyDrawableToInternalStorage(
                context,
                it,
                "image$it.png"
            )
            PhotoChallengeUserEntity(
                id = UUID.randomUUID().toString(),
                lastname = "User $it",
                email = "email $it",
                password = "password $it",
                picturePath = imagePah,
                votingCount = (0..8).random(),
                remainingVotes = 5
            )
        }
        CoroutineScope(Dispatchers.Default).launch {
            userDao.deleteAllUsers()
            mockUsers.forEach { user ->
                userDao.insertUser(user)
            }
            val currentUser = authRepository.currentUserId
            userDao.insertUser()
        }
    }

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

    override fun getUsers(): Flow<Result<List<PhotoChallengeUser>>> = flow {
        val users = userDao.getAllUsers()
        emit(Result.success(users.map { it.toPhotoChallengeUser(imageStorage = imageStorage) }))
    }

    private fun copyDrawableToInternalStorage(
        context: Context,
        drawableId: Int,
        fileName: String
    ): String {
        val bitmap = BitmapFactory.decodeResource(context.resources, drawableId)
        val file = File(context.filesDir, fileName)

        FileOutputStream(file).use { fos ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
        }

        return file.name // Voici le chemin du fichier sur l'appareil
    }
}

sealed interface MockData {
    val emojis: Int
    val userPictures: Set<Int>

    data object Angry : MockData {
        override val emojis: Int
            get() = R.drawable.angry
        override val userPictures: Set<Int>
            get() = setOf(
                R.drawable.angry1,
                R.drawable.angry2,
                R.drawable.angry3,
                R.drawable.enerve
            )
    }

    data object Sad : MockData {
        override val emojis: Int
            get() = R.drawable.pensive
        override val userPictures: Set<Int>
            get() = setOf(R.drawable.sad1, R.drawable.sad2, R.drawable.sad3, R.drawable.sad4)
    }
}