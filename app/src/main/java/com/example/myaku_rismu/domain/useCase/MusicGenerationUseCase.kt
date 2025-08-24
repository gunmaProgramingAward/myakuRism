package com.example.myaku_rismu.domain.useCase

import com.example.myaku_rismu.data.database.entity.MusicGenerationEntity
import kotlinx.coroutines.flow.Flow

interface MusicGenerationUseCase {
    suspend fun saveTodayGeneration(targetId: String)

    suspend fun getTodayTargetId(): String?

    suspend fun isTodayAlreadyGenerated(): Boolean

    fun getTodayGenerationFlow(): Flow<MusicGenerationEntity?>

    suspend fun cleanupOldGenerations()
}