package com.example.myaku_rismu.core.base

import androidx.activity.compose.rememberLauncherForActivityResult
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
import com.example.myaku_rismu.core.ui.dialog.PermissionHealthConnectDialog
import com.example.myaku_rismu.domain.model.PermissionResult

@Composable
fun MainAppScreen(
    appState: AppState,
    modifier: Modifier = Modifier,
    viewModel: MainAppViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    val requiredPermissions = viewModel.getRequiredPermissions()
    val requestPermission = rememberLauncherForActivityResult(
        contract = PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        if (granted.containsAll(requiredPermissions)) {
            // TODO: 現時点では何もしない
        } else {
            viewModel.changeIsShowPermissionHealthConnectDialog(true)
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
                viewModel.launchPlayStoreForHealthConnect(context)
            }

            is PermissionResult.HealthConnectUnavailable -> {
                viewModel.changeHealthConnectUnavailableDialog(true)
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                items = NavigationItem.entries,
                selectedItem = appState.selectedNavigationItem,
                onNavigate = appState::bottomBarNavigateTo,
                modifier = Modifier
            )
        },
        modifier = modifier
    ) { innerPadding ->

        if (uiState.isShowHealthConnectUnavailableDialog) {
            HealthConnectUnavailableDialog(
                onDismiss = { viewModel.changeHealthConnectUnavailableDialog(false) }
            )
        }

        if (uiState.isShowPermissionHealthConnectDialog) {
            PermissionHealthConnectDialog(
                onConfirm = { viewModel.launchSettingApp(context) },
                onDismiss = { viewModel.changeIsShowPermissionHealthConnectDialog(false) }
            )
        }

        AppNavigation(
            appState = appState,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        )
    }
}