package com.example.myaku_rismu.feature.healthDetail

import com.example.myaku_rismu.core.UiEvent

sealed interface HealthDetailUiEvent : UiEvent {
    data class OnClickPeriod(val period: Int) : HealthDetailUiEvent
}