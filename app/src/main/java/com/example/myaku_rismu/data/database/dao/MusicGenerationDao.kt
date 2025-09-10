package com.example.myaku_rismu.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myaku_rismu.data.database.entity.MusicGenerationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicGenerationDao {
    @Query("SELECT * FROM music_generation WHERE createdAt >= :todayStartMs AND createdAt < :tomorrowStartMs")
    suspend fun getTodayGeneration(todayStartMs: Long, tomorrowStartMs: Long): MusicGenerationEntity?
    
    @Query("SELECT * FROM music_generation WHERE createdAt >= :todayStartMs AND createdAt < :tomorrowStartMs")
    fun getTodayGenerationFlow(todayStartMs: Long, tomorrowStartMs: Long): Flow<MusicGenerationEntity?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeneration(generation: MusicGenerationEntity)
    
    @Query("SELECT targetId FROM music_generation WHERE createdAt >= :todayStartMs AND createdAt < :tomorrowStartMs")
    suspend fun getTodayTargetId(todayStartMs: Long, tomorrowStartMs: Long): String?
    
    @Query("DELETE FROM music_generation WHERE createdAt < :cutoffTimeMs")
    suspend fun deleteOldGenerations(cutoffTimeMs: Long)
    
    @Query("SELECT * FROM music_generation ORDER BY createdAt DESC")
    suspend fun getAllGenerations(): List<MusicGenerationEntity>
}