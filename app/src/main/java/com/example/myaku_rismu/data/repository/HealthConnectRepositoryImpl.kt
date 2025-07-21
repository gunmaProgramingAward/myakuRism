package com.example.myaku_rismu.data.repository

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import com.example.myaku_rismu.core.base.constants.HealthConnectConstants.PROVIDER_PACKAGE_NAME
import com.example.myaku_rismu.core.utill.HealthPermissions
import com.example.myaku_rismu.data.datasource.HealthConnectDataSource
import com.example.myaku_rismu.domain.model.HealthConnectAvailability
import com.example.myaku_rismu.domain.model.PermissionResult
import com.example.myaku_rismu.domain.repository.HealthConnectRepository
import com.example.myaku_rismu.feature.setting.ProfileData
import javax.inject.Inject

class HealthConnectRepositoryImpl @Inject constructor(
    private val dataSource: HealthConnectDataSource
) : HealthConnectRepository {
    override suspend fun getHeight (): Double? = dataSource.fetchHeightRecords()
    override suspend fun getWeight(): Double? = dataSource.fetchWeightRecords()
    
    override fun checkHealthConnectAvailability(context: Context): HealthConnectAvailability {
        val status = HealthConnectClient.getSdkStatus(context, PROVIDER_PACKAGE_NAME)
        
        return when (status) {
            HealthConnectClient.SDK_UNAVAILABLE -> HealthConnectAvailability.Unavailable
            HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED -> HealthConnectAvailability.UpdateRequired
            else -> HealthConnectAvailability.Available
        }
    }
    
    override suspend fun checkPermissions(context: Context): PermissionResult {
        val availability = checkHealthConnectAvailability(context)
        
        return when (availability) {
            is HealthConnectAvailability.Available -> {
                val client = HealthConnectClient.getOrCreate(context)
                val allPermissions = HealthPermissions.allPermissions
                val granted = client.permissionController.getGrantedPermissions()
                
                if (granted.containsAll(allPermissions)) {
                    PermissionResult.AllGranted
                } else {
                    val missingPermissions = allPermissions - granted.toSet()
                    PermissionResult.MissingPermissions(missingPermissions)
                }
            }
            is HealthConnectAvailability.Unavailable -> PermissionResult.HealthConnectUnavailable
            is HealthConnectAvailability.UpdateRequired -> PermissionResult.UpdateRequired
        }
    }
}