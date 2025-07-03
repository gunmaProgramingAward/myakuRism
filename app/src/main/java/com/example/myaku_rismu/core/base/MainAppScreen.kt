package com.example.myaku_rismu.core.base

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.navigation.AppNavigation

@Composable
fun MainAppScreen(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    Scaffold { innerPadding ->
        AppNavigation(
            appState = appState,
            modifier = modifier.padding(innerPadding)
        )
    }
}