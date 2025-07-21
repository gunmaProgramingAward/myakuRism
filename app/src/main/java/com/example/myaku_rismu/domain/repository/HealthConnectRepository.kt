package com.example.myaku_rismu.domain.repository

import android.content.Context
import com.example.myaku_rismu.data.model.HealthDataGranularity
import com.example.myaku_rismu.data.model.RecordType
import com.example.myaku_rismu.domain.model.HealthConnectAvailability
import com.example.myaku_rismu.domain.model.PermissionResult
import java.time.Instant

interface HealthConnectRepository {
    fun checkHealthConnectAvailability(context: Context): HealthConnectAvailability
    suspend fun checkPermissions(context: Context): PermissionResult
    suspend fun fetchRecordsByGranularity(
        recordType: RecordType,
        start: Instant,
        granularity: HealthDataGranularity
    ) : List<Long>
}