package com.example.myaku_rismu.feature.setting

import com.example.myaku_rismu.core.ScreenState



data class SettingState(
    val screenState: ScreenState = ScreenState.Initializing(),
    val display: ProfileData = ProfileData(),
    val dialog: SettingDialog? = null,
    val activityLevelIndex: Int = 0,
    val isHealthConnectLinked: Boolean = false,
)

data class ProfileData(
    val birthYear: Int? = null,
    val birthMonth: Int? = null,
    val birthDay: Int? = null,
    val heightCm: Int? = null,
    val weightKg: Int? = null,
    val gender: Int? = null,
)

sealed class SettingDialog {
    data object Birthdate : SettingDialog()
    data object Height : SettingDialog()
    data object Weight : SettingDialog()
    data object Gender : SettingDialog()
}