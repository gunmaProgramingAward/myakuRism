package com.example.myaku_rismu.data.useCase

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.health.connect.client.HealthConnectClient
import com.example.myaku_rismu.core.base.constants.HealthConnectConstants.PROVIDER_PACKAGE_NAME
import com.example.myaku_rismu.core.base.constants.PlayStoreConstants.CALLER_ID_EXTRA
import com.example.myaku_rismu.core.base.constants.PlayStoreConstants.OVERLAY_EXTRA
import com.example.myaku_rismu.core.base.constants.PlayStoreConstants.PACKAGE_NAME
import com.example.myaku_rismu.core.base.constants.PlayStoreConstants.URI_TEMPLATE
import com.example.myaku_rismu.core.utill.HealthPermissions
import com.example.myaku_rismu.domain.model.PermissionResult
import com.example.myaku_rismu.domain.repository.HealthConnectRepository
import com.example.myaku_rismu.domain.useCase.HealthConnectPermissionUseCase
import javax.inject.Inject

class HealthConnectPermissionUseCaseImpl @Inject constructor(
    private val healthConnectRepository: HealthConnectRepository
) : HealthConnectPermissionUseCase {

    override suspend fun checkPermissions(context: Context): PermissionResult {
         val result = healthConnectRepository.checkPermissions(context)
        return when (result) {
            is PermissionResult.AllGranted -> PermissionResult.AllGranted
            is PermissionResult.MissingPermissions ->
                PermissionResult.MissingPermissions(result.missingPermissions)
            is PermissionResult.UpdateRequired -> PermissionResult.UpdateRequired
            else -> PermissionResult.HealthConnectUnavailable
        }
    }

    override fun getRequiredPermissions(): Set<String> {
        return HealthPermissions.allPermissions
    }

    override fun launchPlayStoreForHealthConnect(context: Context) {
        val uriString = URI_TEMPLATE.format(PROVIDER_PACKAGE_NAME)

        context.startActivity(
            Intent(Intent.ACTION_VIEW).apply {
                setPackage(PACKAGE_NAME)
                data = uriString.toUri()
                putExtra(OVERLAY_EXTRA, true)
                putExtra(CALLER_ID_EXTRA, context.packageName)
            }
        )
    }

    override fun launchSettingApp(context: Context) {
        val intent = Intent(HealthConnectClient.ACTION_HEALTH_CONNECT_SETTINGS)
        context.startActivity(intent)
    }
}