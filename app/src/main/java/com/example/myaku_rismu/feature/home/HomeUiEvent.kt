package com.example.myaku_rismu.feature.home

import com.example.myaku_rismu.core.UiEvent

sealed interface HomeUiEvent : UiEvent {
    data class changeBpmPlayerValue(val value: Int) : HomeUiEvent
    data class selectMusicGenre(val metric: HealthMetric) : HomeUiEvent
    data class onSwitchCheckedChange(val isChecked: Boolean) : HomeUiEvent
    data class selectHealthMetric(val type: String) : HomeUiEvent
    data object createNewMusic : HomeUiEvent
    data object ShowBottomSheet : HomeUiEvent
    data object dismissBottomSheet : HomeUiEvent
}