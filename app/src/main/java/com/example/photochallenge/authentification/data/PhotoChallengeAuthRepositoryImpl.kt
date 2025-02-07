package com.example.photochallenge.authentification.data

import com.example.photochallenge.authentification.data.local.dao.PhotoChallengeUserDao
import com.example.photochallenge.authentification.data.local.entity.PhotoChallengeUserEntity
import com.example.photochallenge.authentification.domain.PhotoChallengeAuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class PhotoChallengeAuthRepositoryImpl(
    private val userDao: PhotoChallengeUserDao
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

    override fun logout(): Result<Unit> {
        _currentUserId = null
        return Result.success(Unit)
    }

}