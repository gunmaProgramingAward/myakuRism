package com.example.myaku_rismu.data.datasource

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.myaku_rismu.data.datasource.AggregationUtils.calculateIndex
import com.example.myaku_rismu.data.datasource.AggregationUtils.getTimeRangeSlicer
import com.example.myaku_rismu.data.model.HealthDataGranularity
import java.time.Instant
import java.time.ZoneId

object SleepTimeDataSource {
    suspend fun aggregateSleepTimeByAggregation(
        healthConnectClient: HealthConnectClient,
        granularity: HealthDataGranularity,
        startTime: Instant,
        endTime: Instant,
        zone: ZoneId,
        arraySize: Int
    ): List<Long> {
        val timeRangeSlicer = getTimeRangeSlicer(granularity)

        val request = AggregateGroupByDurationRequest(
            metrics = setOf(SleepSessionRecord.SLEEP_DURATION_TOTAL),
            timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
            timeRangeSlicer = timeRangeSlicer
        )

        val response = healthConnectClient.aggregateGroupByDuration(request)
        val result = LongArray(arraySize)

        response.forEach { aggregationResult ->
            val index = calculateIndex(
                aggregationResult,
                granularity,
                startTime,
                zone,
                arraySize
            )

            if (index in 0 until arraySize) {
                val sleepTotalAvg = aggregationResult.result[SleepSessionRecord.SLEEP_DURATION_TOTAL]
                result[index] = sleepTotalAvg?.toMinutes() ?: 0L
            }
        }
        return result.toList()
    }
}