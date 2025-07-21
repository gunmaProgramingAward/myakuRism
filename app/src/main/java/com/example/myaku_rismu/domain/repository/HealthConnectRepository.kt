package com.example.myaku_rismu.domain.repository

import android.content.Context
import com.example.myaku_rismu.domain.model.HealthConnectAvailability
import com.example.myaku_rismu.domain.model.PermissionResult
import com.example.myaku_rismu.feature.setting.ProfileData

interface HealthConnectRepository {
    fun checkHealthConnectAvailability(context: Context): HealthConnectAvailability
    suspend fun checkPermissions(context: Context): PermissionResult
    suspend fun getHeight(): Double?
    suspend fun getWeight(): Double?
}