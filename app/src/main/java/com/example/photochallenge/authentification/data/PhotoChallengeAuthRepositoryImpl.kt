package com.example.photochallenge.authentification.data

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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class PhotoChallengeAuthRepositoryImpl(
    private val userDao: PhotoChallengeUserDao,
    private val imageStorage: ImageStorage,
    private val context: Context,
) : PhotoChallengeAuthRepository {
    private var _currentUserId: String? = null
    override val currentUserId: String?
        get() = _currentUserId

    override fun createUser(
        lastname: String,
        email: String,
        password: String
    ): Flow<Result<Unit>> = flow {
        try {
            val user = PhotoChallengeUserEntity(
                id = UUID.randomUUID().toString(),
                lastname = lastname,
                email = email,
                password = password,
                remainingVotes = 5,
                picturePath = null
            )
            userDao.insertUser(user)
            _currentUserId = user.id
            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun login(email: String, password: String): Flow<Result<Unit>> = flow {
        try {
            val user = userDao.getUser(email, password)
            if (user != null) {
                _currentUserId = user.id
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(Exception("User not found")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun initMockUsers(imageSet: Set<Int>) = flow {
        try {
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
            if (userDao.getAllUsers().isEmpty()) {
                mockUsers.forEach {
                    userDao.insertUser(it)
                }
            }
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

    override fun logout(): Result<Unit> {
        _currentUserId = null
        return Result.success(Unit)
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