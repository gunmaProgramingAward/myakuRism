package com.example.myaku_rismu.feature.calender

import java.time.LocalDate

/**
 * 캘린더 화면의 UI 이벤트를 정의하는 sealed interface 입니다.
 */
sealed interface CalenderUiEvent {
    /**
     * 특정 날짜의 건강 데이터를 새로고침(가져오기)하도록 요청하는 이벤트입니다.
     * @param date 데이터를 가져올 기준 날짜.
     */
    data class LoadHealthData(val date: LocalDate) : CalenderUiEvent

    /**
     * 사용자가 캘린더에서 특정 날짜를 선택했을 때 발생하는 이벤트입니다.
     * @param date 선택된 날짜.
     */
    data class OnDateSelected(val date: LocalDate) : CalenderUiEvent

    // 필요에 따라 다른 UI 이벤트 추가 가능
    // 예: 이전 주로 이동, 다음 주로 이동 등
    // object LoadPreviousWeek : CalenderUiEvent
    // object LoadNextWeek : CalenderUiEvent
}
