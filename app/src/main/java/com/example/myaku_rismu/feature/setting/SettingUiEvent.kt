package com.example.myaku_rismu.feature.setting

import com.example.myaku_rismu.core.UiEvent
import com.example.myaku_rismu.data.model.SettingType
import com.example.myaku_rismu.domain.model.ActivityLevel
import com.example.myaku_rismu.domain.model.Gender

sealed interface SettingUiEvent : UiEvent {
    data object DismissDialog : SettingUiEvent
    data class ShowDialog(val dialog: SettingType) : SettingUiEvent
    data class HeightSelected(val height: Int) : SettingUiEvent
    data class WeightSelected(val weight: Int) : SettingUiEvent
    data class GenderSelected(val gender: Gender) : SettingUiEvent
    data class BirthdateSelected(val year: Int, val month: Int, val day: Int) : SettingUiEvent
    data class ActivityLevelSelected(val level: ActivityLevel) : SettingUiEvent
}