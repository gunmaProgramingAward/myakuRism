package com.example.myaku_rismu.feature.healthDetail

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.navigation.HealthDetailRoute

fun NavGraphBuilder.healthDetailScreen(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    composable<HealthDetailRoute> {
        HealthDetailScreen(
            appState = appState,
            modifier = modifier
        )
    }
}