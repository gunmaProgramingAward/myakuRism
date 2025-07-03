package com.example.myaku_rismu.feature.calender

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.navigation.HealthDetailRoute

fun NavGraphBuilder.calenderScreen(appState: AppState) {
    composable<HealthDetailRoute> {
        CalenderScreen(appState = appState)
    }
}