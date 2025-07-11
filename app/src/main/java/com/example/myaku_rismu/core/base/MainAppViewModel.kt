package com.example.myaku_rismu.core.base

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.myaku_rismu.domain.model.PermissionResult
import com.example.myaku_rismu.domain.useCase.HealthConnectPermissionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainAppViewModel @Inject constructor(
    private val healthConnectPermissionUseCase: HealthConnectPermissionUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainAppUiState())
    val uiState: StateFlow<MainAppUiState> = _uiState.asStateFlow()

    suspend fun checkPermissions(context: Context): PermissionResult {
        return healthConnectPermissionUseCase.checkPermissions(context)
    }

    fun getRequiredPermissions(): Set<String> {
        return healthConnectPermissionUseCase.getRequiredPermissions()
    }

    fun launchPlayStoreForHealthConnect(context: Context) {
        healthConnectPermissionUseCase.launchPlayStoreForHealthConnect(context)
    }

    fun launchSettingApp(context: Context) {
        healthConnectPermissionUseCase.launchSettingApp(context)
    }

    fun changeHealthConnectUnavailableDialog(value: Boolean) {
        _uiState.update {
            it.copy(
                isShowHealthConnectUnavailableDialog = value
            )
        }
    }

    fun changeIsShowPermissionHealthConnectDialog(value: Boolean) {
        _uiState.update {
            it.copy(
                isShowPermissionHealthConnectDialog = value
            )
        }
    }
}