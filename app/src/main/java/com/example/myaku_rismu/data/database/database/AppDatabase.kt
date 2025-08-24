package com.example.myaku_rismu.data.database.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myaku_rismu.data.database.dao.MusicGenerationDao
import com.example.myaku_rismu.data.database.entity.MusicGenerationEntity

@Database(
    entities = [MusicGenerationEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun musicGenerationDao(): MusicGenerationDao
    
    companion object {
        const val DATABASE_NAME = "myaku_rismu_database"
    }
}