package com.example.myaku_rismu.feature.calender

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.navigation.CalenderRoute

fun NavGraphBuilder.calenderScreen(appState: AppState) {
    composable<CalenderRoute> {
        CalenderScreen(appState = appState)
    }
}