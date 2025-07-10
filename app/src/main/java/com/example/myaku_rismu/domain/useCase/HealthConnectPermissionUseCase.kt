package com.example.myaku_rismu.domain.useCase

import android.content.Context
import com.example.myaku_rismu.domain.model.PermissionResult

interface HealthConnectPermissionUseCase {
    suspend fun checkPermissions(context: Context): PermissionResult
}