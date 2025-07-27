package com.example.myaku_rismu.data.datasource

import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateGroupByDurationRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.example.myaku_rismu.data.datasource.AggregationUtils.calculateIndex
import com.example.myaku_rismu.data.datasource.AggregationUtils.getTimeRangeSlicer
import com.example.myaku_rismu.data.model.HealthDataGranularity
import java.time.Instant
import java.time.ZoneId

object StepRecordDataSource {

    suspend fun aggregateStepByAggregation(
        healthConnectClient: HealthConnectClient,
        granularity: HealthDataGranularity,
        startTime: Instant,
        endTime: Instant,
        zone: ZoneId,
        arraySize: Int
    ): List<Long> {
        val result = LongArray(arraySize)

        if (granularity == HealthDataGranularity.YEARLY) {
            val monthlyTimeRangeSlicer = getTimeRangeSlicer(HealthDataGranularity.MONTHLY)

            val monthlyRequest = AggregateGroupByDurationRequest(
                metrics = setOf(StepsRecord.COUNT_TOTAL),
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime),
                timeRangeSlicer = monthlyTimeRangeSlicer
            )

            val monthlyResponse = healthConnectClient.aggregateGroupByDuration(monthlyRequest)
            val monthlyTotals = mutableMapOf<Int, MutableList<Long>>()

            monthlyResponse.forEachIndexed { index, aggregationResult ->
                val monthIndex = calculateIndex(
                    aggregationResult,
                    HealthDataGranularity.YEARLY,
                    startTime,
                    zone,
                    arraySize
                )

                if (monthIndex in 0 until arraySize) {
                    val stepCount = aggregationResult.result[StepsRecord.COUNT_TOTAL] ?: 0L
                    monthlyTotals.getOrPut(monthIndex) { mutableListOf() }.add(stepCount)
                }
            }

            monthlyTotals.forEach { (monthIndex, dailySteps) ->
                val nonZeroSteps = dailySteps.filter { it > 0L }
                if (nonZeroSteps.isNotEmpty()) {
                    val average = nonZeroSteps.average().toLong()
                    result[monthIndex] = average
                }
            }
        } else {
            val timeRangeSlicer = getTimeRangeSlicer(granularity)

            val request = AggregateGroupByDurationRequest(
                metrics = setOf(StepsRecord.COUNT_TOTAL),
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
                    val stepCountAvg = aggregationResult.result[StepsRecord.COUNT_TOTAL]
                    result[index] = stepCountAvg ?: 0L
                }
            }
        }

        return result.toList()
    }
}