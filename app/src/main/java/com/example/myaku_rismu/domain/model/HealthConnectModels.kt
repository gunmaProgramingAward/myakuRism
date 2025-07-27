package com.example.myaku_rismu.domain.model

sealed class HealthConnectAvailability {
    data object Available : HealthConnectAvailability()
    data object Unavailable : HealthConnectAvailability()
    data object UpdateRequired : HealthConnectAvailability()
}

sealed class PermissionResult {
    data object AllGranted : PermissionResult()
    data class MissingPermissions(val missingPermissions: Set<String>) : PermissionResult()
    data object HealthConnectUnavailable : PermissionResult()
    data object UpdateRequired : PermissionResult()
} 