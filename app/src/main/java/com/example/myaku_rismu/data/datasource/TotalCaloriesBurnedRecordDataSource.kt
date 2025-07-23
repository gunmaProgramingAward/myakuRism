package com.example.myaku_rismu.data.datasource

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.myaku_rismu.data.model.HealthDataGranularity
import java.time.Instant
import java.time.ZoneId

object TotalCaloriesBurnedRecordDataSource {
    suspend fun aggregateTotalCaloriesByAggregation(
        healthConnectClient: HealthConnectClient,
        granularity: HealthDataGranularity,
        startTime: Instant,
        endTime: Instant,
        zone: ZoneId,
        arraySize: Int
    ): List<Long> {
        val timeRangeSlicer = AggregationUtils.getTimeRangeSlicerForTotalCalories(granularity)
        val now = Instant.now()
        val safeEndTime = minOf(endTime, now)

        val response = healthConnectClient.aggregateGroupByDuration(
            AggregateGroupByDurationRequest(
                metrics = setOf(TotalCaloriesBurnedRecord.ENERGY_TOTAL),
                timeRangeFilter = TimeRangeFilter.between(startTime, safeEndTime),
                timeRangeSlicer = timeRangeSlicer,
            )
        )
        val result = LongArray(arraySize)
        val monthTotals = LongArray(arraySize)
        val monthDays = IntArray(arraySize)

        response.forEach { aggregationResult ->
            val index = AggregationUtils.calculateIndex(
                aggregationResult,
                granularity,
                startTime,
                zone,
                arraySize
            )

            if (index in 0 until arraySize) {
                val totalCalories =
                    aggregationResult.result[TotalCaloriesBurnedRecord.ENERGY_TOTAL]?.inKilocalories?.toLong()
                        ?: 0L
                if (granularity == HealthDataGranularity.YEARLY) {
                    monthTotals[index] += totalCalories
                    monthDays[index] += 1
                } else {
                    result[index] = totalCalories
                }
            }
        }
        if (granularity == HealthDataGranularity.YEARLY) {
            for (i in 0 until arraySize) {
                val daysInMonth = java.time.Month.of(i + 1).length(java.time.Year.now().isLeap)
                result[i] = if (monthDays[i] > 0) monthTotals[i] / daysInMonth else 0L
            }
        }
        return result.toList()
    }
}