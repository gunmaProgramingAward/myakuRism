package com.example.myaku_rismu.feature.setting

import com.example.myaku_rismu.core.UiEvent

sealed interface SettingUiEvent : UiEvent {
    data object DismissDialog : SettingUiEvent
    data class ShowDialog(val dialog: SettingDialog) : SettingUiEvent
    data class BirthdateSelected(val year: Int, val month: Int, val day: Int) : SettingUiEvent
    data class HeightSelected(val height: Int) : SettingUiEvent
    data class WeightSelected(val weight: Int) : SettingUiEvent
    data class GenderSelected(val index: Int) : SettingUiEvent
    data class ActivityLevelSelected(val index: Int) : SettingUiEvent
}