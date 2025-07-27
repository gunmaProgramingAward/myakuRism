package com.example.myaku_rismu.core.base

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.health.connect.client.PermissionController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.navigation.AppNavigation
import com.example.myaku_rismu.core.ui.BottomNavigationBar
import com.example.myaku_rismu.core.ui.NavigationItem
import com.example.myaku_rismu.core.ui.dialog.HealthConnectUnavailableDialog
import com.example.myaku_rismu.core.ui.dialog.HealthConnectUpdateDialog
import com.example.myaku_rismu.core.ui.dialog.PermissionHealthConnectDialog
import com.example.myaku_rismu.domain.model.PermissionResult
import com.example.myaku_rismu.feature.musicDetail.MusicDetailScreen

@Composable
fun MainAppScreen(
    appState: AppState,
    modifier: Modifier = Modifier,
    viewModel: MainAppViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    fun eventHandler(event: MainAppUiEvent) {
        when (event) {
            is MainAppUiEvent.LaunchPlayStoreForHealthConnect -> {
                viewModel.launchPlayStoreForHealthConnect(event.context)
            }

            is MainAppUiEvent.LaunchSettingApp -> {
                viewModel.launchSettingApp(event.context)
            }

            is MainAppUiEvent.ChangeHealthConnectUnavailableDialog -> {
                viewModel.changeHealthConnectUnavailableDialog(event.value)
            }

            is MainAppUiEvent.ChangeIsShowPermissionHealthConnectDialog -> {
                viewModel.changeIsShowPermissionHealthConnectDialog(event.value)
            }

            is MainAppUiEvent.ChangeIsShowHealthConnectUpdateDialog -> {
                viewModel.changeIsShowHealthConnectUpdateDialog(event.value)
            }
        }
    }

    val requiredPermissions = viewModel.getRequiredPermissions()
    val requestPermission = rememberLauncherForActivityResult(
        contract = PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        if (granted.containsAll(requiredPermissions)) {
            // TODO: 現時点では何もしない
        } else {
            eventHandler(MainAppUiEvent.ChangeIsShowPermissionHealthConnectDialog(true))
        }
    }

    LaunchedEffect(Unit) {
        val permissionResult = viewModel.checkPermissions(context)

        when (permissionResult) {
            is PermissionResult.MissingPermissions -> {
                requestPermission.launch(permissionResult.missingPermissions)
            }

            is PermissionResult.AllGranted -> {
                // TODO:　現時点では何もしない
            }

            is PermissionResult.UpdateRequired -> {
                eventHandler(MainAppUiEvent.ChangeIsShowHealthConnectUpdateDialog(true))
            }

            is PermissionResult.HealthConnectUnavailable -> {
                eventHandler(MainAppUiEvent.ChangeHealthConnectUnavailableDialog(true))
            }
        }
    }

    Scaffold(
        bottomBar = {
            Column {
                MusicDetailScreen()
                BottomNavigationBar(
                    items = NavigationItem.entries,
                    selectedItem = appState.selectedNavigationItem,
                    onNavigate = appState::bottomBarNavigateTo,
                    modifier = Modifier
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->

        if (uiState.isShowHealthConnectUnavailableDialog) {
            HealthConnectUnavailableDialog(
                onConfirm = { eventHandler(MainAppUiEvent.LaunchPlayStoreForHealthConnect(context)) },
                onDismiss = {
                    eventHandler(MainAppUiEvent.ChangeHealthConnectUnavailableDialog(true))
                }
            )
        }

        if (uiState.isShowPermissionHealthConnectDialog) {
            PermissionHealthConnectDialog(
                onConfirm = { eventHandler(MainAppUiEvent.LaunchSettingApp(context)) },
                onDismiss = {
                    eventHandler(MainAppUiEvent.ChangeIsShowPermissionHealthConnectDialog(false))
                }
            )
        }

        if (uiState.isShowHealthConnectUpdateDialog) {
            HealthConnectUpdateDialog(
                onConfirm = { eventHandler(MainAppUiEvent.LaunchPlayStoreForHealthConnect(context)) },
                onDismiss = {
                    eventHandler(MainAppUiEvent.ChangeIsShowHealthConnectUpdateDialog(false))
                },
            )
        }

        AppNavigation(
            appState = appState,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        )
    }
}