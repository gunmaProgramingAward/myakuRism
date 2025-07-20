package com.example.myaku_rismu.domain.useCase

import android.content.Context
import com.example.myaku_rismu.domain.model.PermissionResult

interface HealthConnectPermissionUseCase {
    suspend fun checkPermissions(context: Context): PermissionResult
    fun getRequiredPermissions(): Set<String>
    fun launchPlayStoreForHealthConnect(context: Context)
    fun launchSettingApp(context: Context)
}