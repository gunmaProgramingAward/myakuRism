package com.example.myaku_rismu.feature.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.data.model.RecordType
import kotlinx.coroutines.flow.update

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

    fun dismissBottomSheet() {
        _uiState.update { it.copy(showBottomSheet = false) }
    }

    fun createNewMusic() {
        _uiState.update { it.copy(createMusic = true) }
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
                type = RecordType.HEART_RATE,
                currentValue = 200,
                targetValue = 180
            ),
            HealthMetric(
                type = RecordType.STEPS,
                currentValue = 5000,
                targetValue = 10000
            ),
            HealthMetric(
                type = RecordType.CALORIES,
                currentValue = 1200,
                targetValue = 2000
            ),
            HealthMetric(
                type = RecordType.SLEEP_TIME,
                currentValue = 9,
                targetValue = 8
            ),
            HealthMetric(
                type = RecordType.DISTANCE,
                currentValue = 2,
                targetValue = 5
            )
        )
        _uiState.update { it.copy(metrics = metrics) }
    }
}