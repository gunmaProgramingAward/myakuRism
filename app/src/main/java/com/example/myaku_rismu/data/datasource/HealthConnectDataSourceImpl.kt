package com.example.myaku_rismu.data.datasource

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import com.example.myaku_rismu.data.datasource.AggregationUtils.getAggregationRange
import com.example.myaku_rismu.data.model.HealthDataGranularity
import com.example.myaku_rismu.data.model.RecordType
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import java.time.Instant
import java.time.ZoneId

class HealthConnectDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : HealthConnectDataSource {
    private val healthConnectClient by lazy {
        HealthConnectClient.getOrCreate(context)
    }

    override suspend fun fetchRecordsByGranularity(
        recordType: RecordType,
        start: Instant,
        granularity: HealthDataGranularity,
    ): List<Long> {
        val zone = ZoneId.systemDefault()
        val baseDate = start.atZone(zone).toLocalDate()

        val (startTime, endTime, arraySize) = getAggregationRange(granularity, baseDate, zone)

        return when (recordType) {
            RecordType.STEPS -> {
                StepRecordDataSource.aggregateStepByAggregation(
                    healthConnectClient = healthConnectClient,
                    granularity = granularity,
                    startTime = startTime,
                    endTime = endTime,
                    zone = zone,
                    arraySize = arraySize
                )
            }

            RecordType.HEART_RATE -> {
                HeartRateRecordDataSource.aggregateHeartRateByAggregation(
                    healthConnectClient = healthConnectClient,
                    granularity = granularity,
                    startTime = startTime,
                    endTime = endTime,
                    zone = zone,
                    arraySize = arraySize
                )
            }

            RecordType.SLEEP_TIME -> {
                SleepTimeDataSource.aggregateSleepTimeByAggregation(
                    healthConnectClient = healthConnectClient,
                    granularity = granularity,
                    startTime = startTime,
                    endTime = endTime,
                    zone = zone,
                    arraySize = arraySize
                )
            }
            RecordType.DISTANCE -> {
                DistanceDataSource.aggregateDistanceByAggregation(
                    healthConnectClient = healthConnectClient,
                    granularity = granularity,
                    startTime = startTime,
                    endTime = endTime,
                    zone = zone,
                    arraySize = arraySize
                )
            }
            RecordType.CALORIES -> {
                TotalCaloriesBurnedRecordDataSource.aggregateTotalCaloriesByAggregation(
                    healthConnectClient = healthConnectClient,
                    granularity = granularity,
                    startTime = startTime,
                    endTime = endTime,
                    zone = zone,
                    arraySize = arraySize
                )
            }
        }
    }
}