package com.example.myaku_rismu.feature.healthDetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.data.model.RecordType
import com.example.myaku_rismu.domain.useCase.HealthConnectUseCase
import com.example.myaku_rismu.domain.useCase.SettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class HealthDetailViewModel @Inject constructor(
    private val healthConnectUseCase: HealthConnectUseCase,
    private val settingUseCase: SettingUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(HealthDetailState())
    val uiState: StateFlow<HealthDetailState> = _uiState.asStateFlow()
    private val recordType = savedStateHandle.get<RecordType>("recordType")
        ?: RecordType.HEART_RATE

    init {
        _uiState.update {
            it.copy(recordType = recordType)
        }
        updateListData()
        updateHealthType()
        getRecordTypeTarget()
        _uiState.update {
            it.copy(screenState = ScreenState.Success())
        }
    }

    fun changeSelectedPeriod(period: Int) {
        _uiState.update { currentState ->
            currentState.copy(selectedPeriods = period)
        }
        updateListData()
    }

    private fun updateListData() {
        viewModelScope.launch {
            val newDate = withContext(Dispatchers.IO) {
                healthConnectUseCase.fetchRecordsByGranularity(
                    recordType = recordType,
                    start = Instant.now(),
                    granularity = _uiState.value.granularity
                )
            }
            _uiState.update {
                it.copy(
                    listDate = newDate
                )
            }

            updateDailyAverage()
            updateAxisConfig()
        }
    }

    private fun updateDailyAverage() {
        val stepData = _uiState.value.listDate
        val filteredData = stepData.filter { it != 0L }

        val dailyAverage = if (filteredData.isNotEmpty()) {
            filteredData.average().toInt()
        } else {
            0
        }

        _uiState.update { currentState ->
            currentState.copy(dailyAverage = dailyAverage)
        }
    }


    private fun updateHealthType() {
        _uiState.update { currentState ->
            currentState.copy(recordType = recordType)
        }
    }

    private fun updateAxisConfig() {
        val selectedPeriod = _uiState.value.selectedPeriods
        val stepData = _uiState.value.listDate

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

    private fun getRecordTypeTarget() {
        val recordType = _uiState.value.recordType ?: return
        viewModelScope.launch {
            val target = withContext(Dispatchers.IO) {
                settingUseCase.getRecordTypeTarget(recordType)
            } ?: 0
            _uiState.update { it.copy(target = target) }
        }
    }

    fun updateRecordTarget(target: Int) {
        val recordType = uiState.value.recordType ?: return
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                settingUseCase.updateRecordTypeTarget(
                    recordType = recordType,
                    target = target
                )
            }
            _uiState.update {
                it.copy(target = target)
            }
        }
    }

    fun changeIsShowSettingDialog(isShow: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isShowSettingDialog = isShow)
        }
    }
}