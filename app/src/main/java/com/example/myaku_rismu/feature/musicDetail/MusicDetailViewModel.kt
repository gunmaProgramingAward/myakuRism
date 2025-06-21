package com.example.myaku_rismu.feature.musicDetail

import androidx.lifecycle.ViewModel
import com.example.myaku_rismu.core.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import kotlin.math.roundToInt

class MusicDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MusicDetailState())
    val uiState: StateFlow<MusicDetailState> = _uiState.asStateFlow()

    init {
        updateStepData()
        _uiState.update {
            it.copy(
                screenState = ScreenState.Success()
            )
        }
    }

    fun changeSelectedPeriod(period: Int) {
        _uiState.update { currentState ->
            currentState.copy(selectedPeriods = period)
        }
        updateStepData()
    }

    private fun updateStepData() {
        val selectedPeriod = _uiState.value.selectedPeriods
        val newStepData = when (selectedPeriod) {
            // TODO :　仮のデータなので後で置き換え
            0 -> List((1..24).random()) { (10..50).random() }
            1 -> List((1..7).random()) { (200..800).random() }
            2 -> List((1..31).random()) { (200..800).random() }
            3 -> List((1..12).random()) { (6000..25000).random() }
            else -> emptyList()
        }

        _uiState.update {
            it.copy(
                stepData = newStepData,
            )
        }

        updateAxisConfig()
        updateDailyAverage()
    }

    private fun updateDailyAverage() {
        val dailyAverage = calculateDailyAverage(
            uiState.value.stepData,
            uiState.value.selectedPeriods
        )
        _uiState.update { it.copy(dailyAverage = dailyAverage) }
    }

    private fun calculateDailyAverage(stepData: List<Int>, selectedPeriod: Int): String {
        return when (selectedPeriod) {
            0 -> stepData.sum().toString()
            1, 2 -> stepData.average().roundToInt().toString()
            3 -> {
                val totalSteps = stepData.sum().toDouble()
                val daysInYear = LocalDate.now().lengthOfYear().toDouble()
                (totalSteps / daysInYear).roundToInt().toString()
            }
            else -> "0"
        }
    }

    private fun updateAxisConfig() {
        val selectedPeriod = _uiState.value.selectedPeriods
        val stepData = _uiState.value.stepData

        val newAxisConfig = when (selectedPeriod) {
            0 -> AxisConfig(
                maxValue = 24,
                labelFormatter = { "${it}時" },
                spacing = 4,
                totalLabels = 6
            )
            1 -> {
                val weekDays = listOf("日", "月", "火", "水", "木", "金", "土")
                AxisConfig(
                    maxValue = 7,
                    labelFormatter = { weekDays.getOrElse(it) { "" } },
                    spacing = 1,
                    totalLabels = 7
                )
            }
            2 -> AxisConfig(
                maxValue = 31,
                labelFormatter = { "${it + 1}日" },
                spacing = 5,
                totalLabels = 7
            )
            3 -> AxisConfig(
                maxValue = 12,
                labelFormatter = { "${it + 1}" },
                spacing = 1,
                totalLabels = 12
            )
            else -> AxisConfig(
                maxValue = stepData.size.coerceAtLeast(1),
                labelFormatter = { it.toString() },
                spacing = 1,
                totalLabels = stepData.size.coerceAtLeast(1)
            )
        }
        _uiState.update { it.copy(axisConfig = newAxisConfig) }
    }
}