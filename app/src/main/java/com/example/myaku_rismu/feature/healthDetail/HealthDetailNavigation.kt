package com.example.myaku_rismu.feature.healthDetail

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.navigation.CalendarRoute

fun NavGraphBuilder.healthDetailScreen(appState: AppState) {
    composable<CalendarRoute> {
        HealthDetailScreen(appState = appState)
    }
}