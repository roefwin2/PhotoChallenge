package com.example.photochallenge.feature.authentification.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.photochallenge.feature.authentification.data.local.entity.PhotoChallengeUserEntity
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

    @Query("DELETE FROM challenge_users")
    suspend fun deleteAllUsers()

    @Query("UPDATE challenge_users SET votingCount = votingCount + :increment WHERE id = :userId")
    suspend fun incrementVoteCount(userId: String, increment: Int)

    @Query("UPDATE challenge_users SET remainingVotes = remainingVotes + :increment WHERE id = :userId")
    suspend fun updateRemainingVotes(userId: String, increment: Int)

    @Query("UPDATE challenge_users SET picturePath = :picturePath WHERE id = :userId")
    suspend fun updateCurrentPicture(userId: String,picturePath: String)

    @Query("SELECT remainingVotes FROM challenge_users WHERE id = :userId")
    suspend fun getRemainingVotes(userId: String): Int?

    @Query("SELECT * FROM challenge_users")
    suspend fun getAllUsers(): List<PhotoChallengeUserEntity>
}