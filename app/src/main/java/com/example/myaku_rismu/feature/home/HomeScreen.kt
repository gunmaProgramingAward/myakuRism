package com.example.myaku_rismu.feature.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.health.connect.client.PermissionController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.utill.HealthPermissions

@Composable
fun HomeScreen(
    appState: AppState,
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(modifier = modifier) { innerPadding ->
        // TODO: 仮のボタン
        Column {
            Button(
                onClick = {
                    appState.navigateToHealthDetail()
                },
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
            ) {
                Text("Go to Health Detail")
            }

            Button(
                onClick = {
                    viewModel.generateMusic(prompt = "hiphop")
                }
            ) { }
        }
    }
}