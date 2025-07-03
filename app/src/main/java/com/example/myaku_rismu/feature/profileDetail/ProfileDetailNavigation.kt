package com.example.myaku_rismu.feature.profileDetail

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.navigation.ProfileDetailRoute

fun NavGraphBuilder.profileDetailScreen(appState: AppState, modifier: Modifier = Modifier) {
    composable<ProfileDetailRoute> {
        ProfileDetailScreen(appState = appState, modifier = modifier)
    }
}