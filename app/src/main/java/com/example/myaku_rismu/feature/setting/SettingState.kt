package com.example.myaku_rismu.feature.setting

import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.domain.model.SettingData


data class SettingState(
    val screenState: ScreenState = ScreenState.Initializing(),
    val display: SettingData = SettingData(),
    val dialog: SettingDialog? = null,
    val isHealthConnectLinked: Boolean = false,
)


sealed class SettingDialog {
    data object Birthdate : SettingDialog()
    data object Height : SettingDialog()
    data object Weight : SettingDialog()
    data object Gender : SettingDialog()
}