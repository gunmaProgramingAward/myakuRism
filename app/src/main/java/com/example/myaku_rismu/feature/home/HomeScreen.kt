package com.example.myaku_rismu.feature.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myaku_rismu.core.AppState

@Composable
fun HomeScreen(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) { innerPadding ->
        // TODO: 仮のボタン
        Button(
            onClick = {
                appState.navigateToHealthDetail()
            },
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
        ) {
            Text("Go to Health Detail")
        }
    }
}