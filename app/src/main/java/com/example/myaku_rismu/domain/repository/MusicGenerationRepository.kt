package com.example.myaku_rismu.domain.repository

import com.example.myaku_rismu.data.database.entity.MusicGenerationEntity
import kotlinx.coroutines.flow.Flow

interface MusicGenerationRepository {
    suspend fun saveTodayGeneration(targetId: String)
    suspend fun getTodayTargetId(): String?
    suspend fun isTodayAlreadyGenerated(): Boolean
    fun getTodayGenerationFlow(): Flow<MusicGenerationEntity?>
    suspend fun cleanupOldGenerations()
}