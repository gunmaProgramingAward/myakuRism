package com.example.myaku_rismu.data.useCase

import android.content.Context
import com.example.myaku_rismu.domain.model.PermissionResult
import com.example.myaku_rismu.domain.useCase.HealthConnectPermissionUseCase
import com.example.myaku_rismu.domain.repository.HealthConnectRepository
import javax.inject.Inject

class HealthConnectPermissionUseCaseImpl @Inject constructor(
    private val healthConnectRepository: HealthConnectRepository
) : HealthConnectPermissionUseCase {
    
    override suspend fun checkPermissions(context: Context): PermissionResult {
        return healthConnectRepository.checkPermissions(context)
    }
}