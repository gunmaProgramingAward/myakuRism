package com.example.myaku_rismu.feature.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.myaku_rismu.core.AppState

@Composable
fun HomeScreen(
    appState: AppState,
) {
    Button(
        onClick = {
            appState.navigateToHealthDetail()
        }
    ) {
        Text("Go to Health Detail")
    }
}