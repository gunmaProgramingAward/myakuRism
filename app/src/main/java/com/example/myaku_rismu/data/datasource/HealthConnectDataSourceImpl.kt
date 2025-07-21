package com.example.myaku_rismu.data.datasource

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Instant
import javax.inject.Inject

class HealthConnectDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : HealthConnectDataSource {
    override suspend fun fetchHeightRecords(): Double? {
        val client = HealthConnectClient.getOrCreate(context)
        val request = ReadRecordsRequest(
            recordType = HeightRecord::class,
            timeRangeFilter = TimeRangeFilter.after(Instant.EPOCH),
            pageSize = 1
        )
        val records = client.readRecords(request).records
        return records.lastOrNull()?.height?.inMeters
    }

    override suspend fun fetchWeightRecords(): Double? {
        val client = HealthConnectClient.getOrCreate(context)
        val request = ReadRecordsRequest(
            recordType = WeightRecord::class,
            timeRangeFilter = TimeRangeFilter.after(Instant.EPOCH),
            pageSize = 1
        )
        val records = client.readRecords(request).records
        return records.lastOrNull()?.weight?.inKilograms
    }

    override suspend fun fetchHeartRateRecords(): List<Any> {
        return emptyList() // 仮実装
    }
}