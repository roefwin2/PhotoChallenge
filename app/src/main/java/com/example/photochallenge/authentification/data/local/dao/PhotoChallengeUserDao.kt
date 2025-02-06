package com.example.photochallenge.authentification.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.photochallenge.authentification.data.local.entity.PhotoChallengeUserEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface PhotoChallengeUserDao {
    @Query("SELECT * FROM challenge_users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUser(email: String, password: String): PhotoChallengeUserEntity?

    @Query("SELECT * FROM challenge_users WHERE id = :userId")
    fun observeUser(userId: String): Flow<PhotoChallengeUserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: PhotoChallengeUserEntity)

    @Delete
    suspend fun deleteUser(user: PhotoChallengeUserEntity)
}