package com.example.myaku_rismu.core.navigation

import com.example.myaku_rismu.data.model.RecordType
import kotlinx.serialization.Serializable


@Serializable
data object HomeRoute
@Serializable
data object SettingsRoute
@Serializable
data object ProfileDetailRoute
@Serializable
data object CalenderRoute
@Serializable
data object LibraryRoute
@Serializable
data class HealthDetailRoute(val recordType: RecordType = RecordType.HEART_RATE)

