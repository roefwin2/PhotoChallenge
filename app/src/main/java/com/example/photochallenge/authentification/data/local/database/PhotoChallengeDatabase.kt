package com.example.photochallenge.authentification.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.photochallenge.authentification.data.local.dao.PhotoChallengeUserDao
import com.example.photochallenge.authentification.data.local.entity.PhotoChallengeUserEntity

@Database(
    entities = [PhotoChallengeUserEntity::class],
    version = 4,
    exportSchema = false
)
abstract class PhotoChallengeDatabase : RoomDatabase() {
    abstract fun userDao(): PhotoChallengeUserDao
}