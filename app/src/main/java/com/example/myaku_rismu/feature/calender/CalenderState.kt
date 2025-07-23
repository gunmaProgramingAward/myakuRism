package com.example.myaku_rismu.feature.calender // ViewModel과 같은 패키지 또는 하위 패키지에 위치

import java.time.LocalDate

/**
 * 캘린더 화면의 UI 상태를 나타내는 데이터 클래스입니다.
 *
 * @param isLoading 데이터 로딩 중인지 여부.
 * @param error 오류 메시지 (오류 발생 시).
 * @param selectedDate 현재 선택된 날짜 (캘린더에서 날짜를 선택하는 기능이 있다면 활용).
 * @param weeklySteps 주간 걸음 수 데이터 (월요일부터 일요일까지, 각 요일별 값).
 * @param weeklyCalories 주간 소모 칼로리 데이터.
 * @param weeklyDistance 주간 이동 거리 데이터.
 * @param weeklyHeartRate 주간 심박수 데이터 (평균값 또는 대표값).
 */
data class CalenderState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedDate: LocalDate = LocalDate.now(), // 기본값은 오늘 날짜
    val weeklySteps: List<Long> = List(7) { 0L }, // 7일치 데이터를 0으로 초기화
    val weeklyCalories: List<Long> = List(7) { 0L },
    val weeklyDistance: List<Long> = List(7) { 0L },
    val weeklyHeartRate: List<Long> = List(7) { 0L },
    val weeklySleep: List<Long> = List(7) { 0L }
    // 필요에 따라 다른 건강 데이터나 UI 상태 추가 가능
)
