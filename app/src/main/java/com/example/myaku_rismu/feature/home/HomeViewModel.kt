package com.example.myaku_rismu.feature.home

import androidx.lifecycle.ViewModel
import com.example.myaku_rismu.core.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    init {
        updateMetrics()
        _uiState.update {
            it.copy(
                screenState = ScreenState.Success()
            )
        }
    }

    fun showBottomSheet() {
        _uiState.update { it.copy(showBottomSheet = true) }
    }

    fun hideBottomSheet() {
        _uiState.update { it.copy(showBottomSheet = false) }
    }

    fun selectMusicGenre(metric: HealthMetric) {
        _uiState.update {
            it.copy(selectedGenre = metric)
        }
    }

    fun onSwitchCheckedChange(isChecked: Boolean) {
        _uiState.update {
            it.copy(
                isSwitchChecked = isChecked,
            )
        }
    }

    fun changeBpmPlayerValue(value: Int) {
        _uiState.update {
            it.copy(
                bpmPlayerValue = value
            )
        }
    }

    private fun updateMetrics() {
        // 仮データ
        val metrics = listOf(
            HealthMetric(
                type = HomeHealthType.HeartRate,
                currentValue = 200,
                targetValue = 180
            ),
            HealthMetric(
                type = HomeHealthType.Walk,
                currentValue = 5000,
                targetValue = 10000
            ),
            HealthMetric(
                type = HomeHealthType.Move,
                currentValue = 1200,
                targetValue = 2000
            ),
            HealthMetric(
                type = HomeHealthType.SleepTime,
                currentValue = 9,
                targetValue = 8
            ),
            HealthMetric(
                type = HomeHealthType.MoveDistance,
                currentValue = 2,
                targetValue = 5
            )
        )
        _uiState.update { it.copy(metrics = metrics) }

    }
}