package com.example.myaku_rismu.core.base

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.navigation.AppNavigation
import com.example.myaku_rismu.core.ui.BottomNavigationBar
import com.example.myaku_rismu.core.ui.NavigationItem

@Composable
fun MainAppScreen(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = NavigationItem.entries,
                selectedItem = appState.selectedNavigationItem,
                onNavigate =appState::bottomBarNavigateTo,
                modifier = Modifier
            )
        },
        modifier = modifier
    ) { innerPadding ->
        AppNavigation(
            appState = appState,
            modifier = Modifier.padding(innerPadding)
        )
    }
}