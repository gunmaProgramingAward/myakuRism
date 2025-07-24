package com.example.myaku_rismu.feature.setting

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.navigation.SettingsRoute

fun NavGraphBuilder.settingScreen(appState: AppState, modifier: Modifier = Modifier) {
    composable<SettingsRoute> {
        SettingScreen(/*appState = appState*/ modifier = modifier)
    }
}