package com.example.myaku_rismu.feature.calender

import java.time.LocalDate

sealed interface CalenderUiEvent {
    data class LoadHealthData(val date: LocalDate) : CalenderUiEvent

    data class OnDateSelected(val date: LocalDate) : CalenderUiEvent
}
