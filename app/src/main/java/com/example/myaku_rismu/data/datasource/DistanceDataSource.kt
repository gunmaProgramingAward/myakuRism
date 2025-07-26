package com.example.myaku_rismu.data.datasource

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.myaku_rismu.data.datasource.AggregationUtils.calculateIndex
import com.example.myaku_rismu.data.datasource.AggregationUtils.getTimeRangeSlicer
import com.example.myaku_rismu.data.model.HealthDataGranularity
import java.time.Instant
import java.time.ZoneId

object DistanceDataSource {
    suspend fun aggregateDistanceByAggregation(
        healthConnectClient: HealthConnectClient,
        granularity: HealthDataGranularity,
        startTime: Instant,
        endTime: Instant,
        zone: ZoneId,
        arraySize: Int
    ) : List<Long> {
        val result = LongArray(arraySize)

        if (granularity == HealthDataGranularity.YEARLY) {
            val monthlyTimeRangeSlicer = getTimeRangeSlicer(HealthDataGranularity.MONTHLY)

            val monthlyRequest = AggregateGroupByDurationRequest(
                metrics = setOf(DistanceRecord.DISTANCE_TOTAL),
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
                timeRangeSlicer = monthlyTimeRangeSlicer
            )

            val monthlyResponse = healthConnectClient.aggregateGroupByDuration(monthlyRequest)
            val monthlyTotals = mutableMapOf<Int, MutableList<Long>>()

            monthlyResponse.forEach { aggregationResult ->
                val monthIndex = calculateIndex(
                    aggregationResult,
                    HealthDataGranularity.YEARLY,
                    startTime,
                    zone,
                    arraySize
                )

                if (monthIndex in 0 until arraySize) {
                    val distance = aggregationResult.result[DistanceRecord.DISTANCE_TOTAL]?.inMeters?.toLong() ?: 0L
                    monthlyTotals.getOrPut(monthIndex) { mutableListOf() }.add(distance)
                }
            }

            monthlyTotals.forEach { (monthIndex, dailyDistances) ->
                val nonZeroDistances = dailyDistances.filter { it > 0L }
                if (nonZeroDistances.isNotEmpty()) {
                    result[monthIndex] = nonZeroDistances.average().toLong()
                }
            }
        } else {
            val timeRangeSlicer = getTimeRangeSlicer(granularity)

            val request = AggregateGroupByDurationRequest(
                metrics = setOf(DistanceRecord.DISTANCE_TOTAL),
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
                timeRangeSlicer = timeRangeSlicer
            )

            val response = healthConnectClient.aggregateGroupByDuration(request)

            response.forEach { aggregationResult ->
                val index = calculateIndex(
                    aggregationResult,
                    granularity,
                    startTime,
                    zone,
                    arraySize
                )

                if (index in 0 until arraySize) {
                    val distanceAvg = aggregationResult.result[DistanceRecord.DISTANCE_TOTAL]
                    result[index] = distanceAvg?.inMeters ?.toLong() ?: 0L
                }
            }
        }

        return result.toList()
    }
}