package com.example.myaku_rismu.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaku_rismu.core.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.data.model.HealthDataGranularity
import com.example.myaku_rismu.data.model.RecordType
import com.example.myaku_rismu.domain.useCase.HealthConnectUseCase
import com.example.myaku_rismu.domain.useCase.SettingUseCase
import com.example.myaku_rismu.domain.useCase.MusicGenerationUseCase
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val healthConnectUseCase: HealthConnectUseCase,
    private val settingUseCase: SettingUseCase,
    private val musicGenerationUseCase: MusicGenerationUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update { it.copy(screenState = ScreenState.Success()) }
        }
    }

    fun showBottomSheet() {
        _uiState.update { it.copy(showBottomSheet = true) }
    }

    fun dismissBottomSheet() {
        _uiState.update { it.copy(showBottomSheet = false) }
    }

    fun createNewMusic(appState: AppState) {
        viewModelScope.launch {
            if (_uiState.value.isEnabledCreateMusic) {
                _uiState.value.selectedGenre?.let { genre ->
                    appState.musicGeneration(
                        recordType = genre.type,
                        bpm = _uiState.value.bpmPlayerValue,
                        instrumental = _uiState.value.isInstrumental
                    )
                }
                _uiState.update {
                    it.copy(
                        isEnabledCreateMusic = false
                    )
                }
            }
        }
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


    private suspend fun fetchMetricValues(startOfDay: Instant): Map<RecordType, Int> {
        return mapOf(
            RecordType.HEART_RATE to healthConnectUseCase.fetchRecordsByGranularity(
                recordType = RecordType.HEART_RATE,
                start = startOfDay,
                granularity = HealthDataGranularity.HOURLY
            ).filter { it != 0L }.average().toInt(),

            RecordType.STEPS to healthConnectUseCase.fetchRecordsByGranularity(
                recordType = RecordType.STEPS,
                start = startOfDay,
                granularity = HealthDataGranularity.HOURLY
            ).sum().toInt(),

            RecordType.CALORIES to healthConnectUseCase.fetchRecordsByGranularity(
                recordType = RecordType.CALORIES,
                start = startOfDay,
                granularity = HealthDataGranularity.HOURLY
            ).sum().toInt(),

            RecordType.SLEEP_TIME to healthConnectUseCase.fetchRecordsByGranularity(
                recordType = RecordType.SLEEP_TIME,
                start = startOfDay,
                granularity = HealthDataGranularity.HOURLY
            ).sum().toInt(),

            RecordType.DISTANCE to healthConnectUseCase.fetchRecordsByGranularity(
                recordType = RecordType.DISTANCE,
                start = startOfDay,
                granularity = HealthDataGranularity.HOURLY
            ).sum().toInt()
        )
    }

    private suspend fun fetchTargetValues(): Map<RecordType, Int> {
        return mapOf(
            RecordType.HEART_RATE to (settingUseCase.getHeartRateTarget() ?: 0),
            RecordType.STEPS to (settingUseCase.getStepsTarget() ?: 0),
            RecordType.CALORIES to (settingUseCase.getCaloriesTarget() ?: 0),
            RecordType.SLEEP_TIME to (settingUseCase.getSleepTimeTarget() ?: 0),
            RecordType.DISTANCE to (settingUseCase.getDistanceTarget() ?: 0)
        )
    }

    suspend fun updateMetrics() {
        val startOfDay = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
        val values = fetchMetricValues(startOfDay)
        val targets = fetchTargetValues()

        val metrics = RecordType.entries.map { type ->
            HealthMetric(
                type = type,
                currentValue = values[type] ?: 0,
                targetValue = targets[type] ?: 0
            )
        }
        _uiState.update { it.copy(metrics = metrics) }
    }

    suspend fun checkIsEnableCreateMusic() {
        val anyExceeded = _uiState.value.metrics.any { it.progress >= 1f }
        val isTodayAlreadyGenerated = musicGenerationUseCase.isTodayAlreadyGenerated()

        _uiState.update { it.copy(isEnabledCreateMusic = anyExceeded && !isTodayAlreadyGenerated) }
    }
}