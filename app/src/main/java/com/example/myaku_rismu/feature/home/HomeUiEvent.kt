package com.example.myaku_rismu.feature.home

import com.example.myaku_rismu.core.UiEvent

sealed interface HomeUiEvent : UiEvent {
    data class changeBpmPlayerValue(val value: Int) : HomeUiEvent
}