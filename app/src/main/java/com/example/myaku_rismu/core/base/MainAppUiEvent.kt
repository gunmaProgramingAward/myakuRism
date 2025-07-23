package com.example.myaku_rismu.core.base

import android.content.Context
import com.example.myaku_rismu.core.UiEvent

sealed interface MainAppUiEvent : UiEvent {
    data class LaunchPlayStoreForHealthConnect(val context: Context) : MainAppUiEvent
    data class LaunchSettingApp(val context: Context) : MainAppUiEvent
    data class ChangeHealthConnectUnavailableDialog(val value: Boolean) : MainAppUiEvent
    data class ChangeIsShowPermissionHealthConnectDialog(val value: Boolean) : MainAppUiEvent
    data class ChangeIsShowHealthConnectUpdateDialog(val value: Boolean) : MainAppUiEvent
}