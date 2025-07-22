package com.example.myaku_rismu.data.datasource

import androidx.health.connect.client.aggregate.AggregationResultGroupedByDuration
import com.example.myaku_rismu.data.model.HealthDataGranularity
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

object AggregationUtils {

    fun getTimeRangeSlicer(granularity: HealthDataGranularity): Duration {
        return when (granularity) {
            HealthDataGranularity.HOURLY -> Duration.ofHours(1)
            HealthDataGranularity.WEEKLY -> Duration.ofDays(1)
            HealthDataGranularity.MONTHLY -> Duration.ofDays(1)
            HealthDataGranularity.YEARLY -> Duration.ofDays(30)
        }
    }

    fun getTimeRangeSlicerForTotalCalories(granularity: HealthDataGranularity): Duration {
        return when (granularity) {
            HealthDataGranularity.HOURLY -> Duration.ofHours(1)
            HealthDataGranularity.WEEKLY -> Duration.ofDays(1)
            HealthDataGranularity.MONTHLY -> Duration.ofDays(1)
            HealthDataGranularity.YEARLY -> Duration.ofDays(1)
        }
    }

    fun calculateIndex(
        aggregationResult: AggregationResultGroupedByDuration,
        granularity: HealthDataGranularity,
        startTime: Instant,
        zone: ZoneId,
        arraySize: Int
    ): Int {
        val periodStart = aggregationResult.startTime.atZone(zone).toLocalDate()
        return when (granularity) {
            HealthDataGranularity.HOURLY -> aggregationResult.startTime.atZone(zone).hour
            HealthDataGranularity.WEEKLY, HealthDataGranularity.MONTHLY -> {
                Duration.between(startTime, aggregationResult.startTime).toDays().toInt()
                    .coerceIn(0, arraySize - 1)
            }
            HealthDataGranularity.YEARLY -> periodStart.monthValue - 1
        }
    }

    fun getAggregationRange(
        granularity: HealthDataGranularity,
        baseDate: java.time.LocalDate,
        zone: ZoneId
    ): Triple<Instant, Instant, Int> {
        return when (granularity) {
            HealthDataGranularity.HOURLY -> {
                val startOfDay = baseDate.atStartOfDay(zone).toInstant()
                val endOfDay = baseDate.plusDays(1).atStartOfDay(zone).toInstant()
                Triple(startOfDay, endOfDay, 24)
            }

            HealthDataGranularity.WEEKLY -> {
                val weekStart = baseDate.minusDays((baseDate.dayOfWeek.value % 7).toLong())
                val startOfWeek = weekStart.atStartOfDay(zone).toInstant()
                val endOfWeek = weekStart.plusDays(7).atStartOfDay(zone).toInstant()
                Triple(startOfWeek, endOfWeek, 7)
            }

            HealthDataGranularity.MONTHLY -> {
                val monthStart = baseDate.withDayOfMonth(1)
                val daysInMonth = baseDate.lengthOfMonth()
                val startOfMonth = monthStart.atStartOfDay(zone).toInstant()
                val endOfMonth = monthStart.plusMonths(1).atStartOfDay(zone).toInstant()
                Triple(startOfMonth, endOfMonth, daysInMonth)
            }

            HealthDataGranularity.YEARLY -> {
                val yearStart = baseDate.withDayOfYear(1)
                val startOfYear = yearStart.atStartOfDay(zone).toInstant()
                val endOfYear = yearStart.plusYears(1).atStartOfDay(zone).toInstant()
                Triple(startOfYear, endOfYear, 12)
            }
        }
    }
}