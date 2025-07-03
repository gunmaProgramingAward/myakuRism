package com.example.myaku_rismu.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.feature.calender.calenderScreen
import com.example.myaku_rismu.feature.healthDetail.healthDetailScreen
import com.example.myaku_rismu.feature.home.HomeScreen
import com.example.myaku_rismu.feature.home.homeScreen
import com.example.myaku_rismu.feature.library.libraryScreen
import com.example.myaku_rismu.feature.profileDetail.profileDetailScreen
import com.example.myaku_rismu.feature.setting.settingScreen

@Composable
fun AppNavigation(
    appState: AppState,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = HomeRoute(),
        modifier = modifier
    ) {
        homeScreen(appState = appState)
        settingScreen(appState = appState)
        profileDetailScreen(appState = appState)
        calenderScreen(appState = appState)
        libraryScreen(appState = appState)
        healthDetailScreen(appState = appState)
    }
}
