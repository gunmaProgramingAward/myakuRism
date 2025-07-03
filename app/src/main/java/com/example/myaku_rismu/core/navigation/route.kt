package com.example.myaku_rismu.core.navigation

import kotlinx.serialization.Serializable


@Serializable
data class HomeRoute(val userId: String? = null)

@Serializable data object SettingsRoute
@Serializable data object ProfileDetailRoute
@Serializable data object MusicDetailRoute
@Serializable data object CalendarRoute
@Serializable data object LibraryRoute
@Serializable data object HealthDetailRoute

