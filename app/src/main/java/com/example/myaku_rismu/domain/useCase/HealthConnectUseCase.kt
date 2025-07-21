package com.example.myaku_rismu.domain.useCase

import com.example.myaku_rismu.data.model.HealthDataGranularity
import com.example.myaku_rismu.data.model.RecordType
import java.time.Instant

interface HealthConnectUseCase {
    suspend fun fetchRecordsByGranularity(
        recordType: RecordType,
        start: Instant,
        granularity: HealthDataGranularity
    ): List<Long>
}