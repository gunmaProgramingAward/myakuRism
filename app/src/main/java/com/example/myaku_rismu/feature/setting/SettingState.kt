package com.example.myaku_rismu.feature.setting

import com.example.myaku_rismu.core.ScreenState
import java.util.Calendar

data class SettingState(
    val screenState: ScreenState = ScreenState.Initializing(),
    val birthYear: Int? = null,
    val birthMonth: Int? = null,
    val birthDay: Int? = null,
    val heightCm: Int? = null,
    val weightKg: Int? = null,
    val genderIndex: Int? = null,
    val dialog: SettingDialog? = null,
    val activityLevelIndex: Int = 0,
    val heightOptions: List<String> = (100..220).map { it.toString() },
    val weightOptions: List<String> = (30..150).map { it.toString() },
    val initialBirthYear: Int = Calendar.getInstance().get(Calendar.YEAR) - 25,
    val initialBirthMonth: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
    val initialBirthDay: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
)

sealed class SettingDialog {
    data object Birthdate : SettingDialog()
    data object Height : SettingDialog()
    data object Weight : SettingDialog()
    data object Gender : SettingDialog()
}