package com.example.myaku_rismu.data.useCase

import com.example.myaku_rismu.data.model.HealthDataGranularity
import com.example.myaku_rismu.data.model.RecordType
import com.example.myaku_rismu.domain.model.HealthDataCacheEntry
import com.example.myaku_rismu.domain.repository.HealthConnectRepository
import com.example.myaku_rismu.domain.useCase.HealthConnectUseCase
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

class HealthConnectUseCaseImpl @Inject constructor(
    private val healthConnectRepository: HealthConnectRepository
) : HealthConnectUseCase {
    private val cache = mutableMapOf<String, HealthDataCacheEntry>()
    private val cacheMutex = Mutex()

    override suspend fun fetchRecordsByGranularity(
        recordType: RecordType,
        start: Instant,
        granularity: HealthDataGranularity
    ): List<Long> {
        val cacheKey = createCacheKey(recordType, start, granularity)
        
        return cacheMutex.withLock {
            val cachedEntry = cache[cacheKey]
            if (cachedEntry != null && !cachedEntry.isExpired()) {
                return@withLock cachedEntry.data
            }

            try {
                val newData = healthConnectRepository.fetchRecordsByGranularity(
                    recordType = recordType,
                    start = start,
                    granularity = granularity
                )

                cache[cacheKey] = HealthDataCacheEntry(
                    data = newData,
                    timestamp = Instant.now()
                )

                if (cache.size > 100) {
                    cleanupExpiredCache()
                }

                newData
            } catch (e: Exception) {
                cachedEntry?.data ?: emptyList()
            }
        }
    }

    private fun createCacheKey(
        recordType: RecordType,
        start: Instant,
        granularity: HealthDataGranularity
    ): String {
        val zone = ZoneId.systemDefault()
        val dateString = when (granularity) {
            HealthDataGranularity.HOURLY -> start.atZone(zone).toLocalDate().toString()
            HealthDataGranularity.WEEKLY -> {
                val baseDate = start.atZone(zone).toLocalDate()
                val weekStart = baseDate.minusDays((baseDate.dayOfWeek.value % 7).toLong())
                weekStart.toString()
            }
            HealthDataGranularity.MONTHLY -> {
                val date = start.atZone(zone).toLocalDate()
                "${date.year}-${String.format(java.util.Locale.US, "%02d", date.monthValue)}"
            }
            HealthDataGranularity.YEARLY -> start.atZone(zone).year.toString()
        }
        return "${recordType.name}-$dateString-${granularity.name}"
    }

    private fun cleanupExpiredCache() {
        val expiredKeys = cache.filterValues { it.isExpired() }.keys
        expiredKeys.forEach { cache.remove(it) }
    }
}