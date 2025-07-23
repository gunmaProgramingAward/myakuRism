package com.example.myaku_rismu.feature.home

import com.example.myaku_rismu.core.UiEvent

sealed interface HomeUiEvent : UiEvent {
    data class ChangeBpmPlayerValue(val value: Int) : HomeUiEvent
    data class SelectMusicGenre(val metric: HealthMetric) : HomeUiEvent
    data class OnSwitchCheckedChange(val isChecked: Boolean) : HomeUiEvent
    data class SelectHealthMetric(val type: String) : HomeUiEvent
    data object CreateNewMusic : HomeUiEvent
    data object ShowBottomSheet : HomeUiEvent
    data object DismissBottomSheet : HomeUiEvent
}