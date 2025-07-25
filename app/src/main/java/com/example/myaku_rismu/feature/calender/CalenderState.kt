package com.example.myaku_rismu.feature.calender // ViewModel과 같은 패키지 또는 하위 패키지에 위치

import java.time.LocalDate

data class CalenderState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedDate: LocalDate = LocalDate.now(),
    val weeklySteps: List<Long> = List(7) { 0L },
    val weeklyCalories: List<Long> = List(7) { 0L },
    val weeklyDistance: List<Long> = List(7) { 0L },
    val weeklyHeartRate: List<Long> = List(7) { 0L },
    val weeklySleep: List<Long> = List(7) { 0L }
)
