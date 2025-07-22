package com.example.myaku_rismu.data.datasource

import com.example.myaku_rismu.data.model.HealthDataGranularity
import com.example.myaku_rismu.data.model.RecordType
import java.time.Instant

interface HealthConnectDataSource {
    suspend fun fetchRecordsByGranularity(
        recordType: RecordType,
        start: Instant,
        granularity: HealthDataGranularity,
    ): List<Long>
}