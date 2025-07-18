package com.example.myaku_rismu.feature.library

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.navigation.LibraryRoute

fun NavGraphBuilder.libraryScreen(appState: AppState, modifier: Modifier = Modifier) {
    composable<LibraryRoute> {
        LibraryScreen(appState = appState, modifier = modifier)
    }
}