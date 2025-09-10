package com.example.myaku_rismu.data.useCase

import com.example.myaku_rismu.data.database.entity.MusicGenerationEntity
import com.example.myaku_rismu.domain.repository.MusicGenerationRepository
import com.example.myaku_rismu.domain.useCase.MusicGenerationUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MusicGenerationUseCaseImpl @Inject constructor(
    private val musicGenerationRepository: MusicGenerationRepository
): MusicGenerationUseCase {

    override suspend fun saveTodayGeneration(targetId: String) {
        musicGenerationRepository.saveTodayGeneration(targetId)
    }

    override suspend fun getTodayTargetId(): String? {
        return musicGenerationRepository.getTodayTargetId()
    }

    override suspend fun isTodayAlreadyGenerated(): Boolean {
        return musicGenerationRepository.isTodayAlreadyGenerated()
    }

    override fun getTodayGenerationFlow(): Flow<MusicGenerationEntity?> {
        return musicGenerationRepository.getTodayGenerationFlow()
    }

    override suspend fun cleanupOldGenerations() {
        return musicGenerationRepository.cleanupOldGenerations()
    }
}