package com.example.myaku_rismu.data.repository

import com.example.myaku_rismu.data.database.dao.MusicGenerationDao
import com.example.myaku_rismu.data.database.entity.MusicGenerationEntity
import com.example.myaku_rismu.domain.repository.MusicGenerationRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicGenerationRepositoryImpl @Inject constructor(
    private val dao: MusicGenerationDao
) : MusicGenerationRepository {

    private fun getTodayTimestamps(): Pair<Long, Long> {
        val today = LocalDate.now()
        val todayStart = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val tomorrowStart = today.plusDays(1).atStartOfDay(ZoneId.systemDefault())
            .toInstant().toEpochMilli()
        return todayStart to tomorrowStart
    }

    override suspend fun saveTodayGeneration(targetId: String) {
        dao.insertGeneration(MusicGenerationEntity.create(targetId))
    }

    override suspend fun getTodayTargetId(): String? {
        val (todayStart, tomorrowStart) = getTodayTimestamps()
        return dao.getTodayTargetId(todayStart, tomorrowStart)
    }

    override suspend fun isTodayAlreadyGenerated(): Boolean {
        val (todayStart, tomorrowStart) = getTodayTimestamps()
        return dao.getTodayGeneration(todayStart, tomorrowStart) != null
    }

    override fun getTodayGenerationFlow(): Flow<MusicGenerationEntity?> {
        val (todayStart, tomorrowStart) = getTodayTimestamps()
        return dao.getTodayGenerationFlow(todayStart, tomorrowStart)
    }

    override suspend fun cleanupOldGenerations() {
        val cutoffTime =
            LocalDate.now().minusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant()
                .toEpochMilli()
        dao.deleteOldGenerations(cutoffTime)
    }
}