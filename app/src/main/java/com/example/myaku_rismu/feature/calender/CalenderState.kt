package com.example.myaku_rismu.feature.calender

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.data.model.RecordType
import java.time.LocalDate

data class CalenderState(
    val screenState: ScreenState = ScreenState.Initializing(),
    val selectedDate: LocalDate = LocalDate.now(),
    val weeklySteps: List<Long> = List(7) { 0L },
    val weeklyCalories: List<Long> = List(7) { 0L },
    val weeklyDistance: List<Long> = List(7) { 0L },
    val weeklyHeartRate: List<Long> = List(7) { 0L },
    val weeklySleep: List<Long> = List(7) { 0L }
)

data class HealthData(
    val type: RecordType,
    val name: String,
    val icon: ImageVector,
    val primaryColor: Color,
    val unit: String,
    val currentValue: Float,
    val goalValue: Float
) {
    val progress: Float
        get() = (currentValue / goalValue).coerceIn(0f, 1f)
}

data class DailyHealthReport(
    val calories: HealthData,
    val heartRate: HealthData,
    val sleepTime: HealthData,
    val steps: HealthData,
    val distance: HealthData
) {
    fun getByType(type: RecordType): HealthData = when (type) {
        RecordType.CALORIES -> calories
        RecordType.HEART_RATE -> heartRate
        RecordType.SLEEP_TIME -> sleepTime
        RecordType.STEPS -> steps
        RecordType.DISTANCE -> distance
    }
}