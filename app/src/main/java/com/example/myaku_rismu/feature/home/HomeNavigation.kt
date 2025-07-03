package com.example.myaku_rismu.feature.home

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.navigation.HomeRoute

fun NavGraphBuilder.homeScreen(appState: AppState) {
    composable<HomeRoute> {
        HomeScreen(appState = appState)
    }
}