package com.example.myaku_rismu.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.data.model.HealthDataGranularity
import com.example.myaku_rismu.data.model.RecordType
import com.example.myaku_rismu.domain.useCase.HealthConnectUseCase
import com.example.myaku_rismu.feature.calender.CalenderState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class CalenderViewModel @Inject constructor(
    private val useCase: HealthConnectUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CalenderState())
    val uiState: StateFlow<CalenderState> = _uiState.asStateFlow()

    init {
        getHealthData(LocalDate.now())
    }

    fun clearError() {
        _uiState.update { currentState ->
            currentState.copy(screenState = ScreenState.Success())
        }
    }

    fun selectDate(date: LocalDate) {
        _uiState.update { currentState ->
            currentState.copy(selectedDate = date)
        }
    }

    fun getHealthData(dateForweek: LocalDate) {
        _uiState.update { currentState ->
            currentState.copy(screenState = ScreenState.Initializing(true))
        }
        viewModelScope.launch {
            try {
                val stepsData = useCase.fetchRecordsByGranularity(
                    start = Instant.now(),
                    recordType = RecordType.STEPS,
                    granularity = HealthDataGranularity.WEEKLY
                )
                val caloriesData = useCase.fetchRecordsByGranularity(
                    start = Instant.now(),
                    recordType = RecordType.CALORIES,
                    granularity = HealthDataGranularity.WEEKLY
                )
                val distanceData = useCase.fetchRecordsByGranularity(
                    start = Instant.now(),
                    recordType = RecordType.DISTANCE,
                    granularity = HealthDataGranularity.WEEKLY
                )
                val heartRateData = useCase.fetchRecordsByGranularity(
                    start = Instant.now(),
                    recordType = RecordType.HEART_RATE,
                    granularity = HealthDataGranularity.WEEKLY
                )
                val sleepData = useCase.fetchRecordsByGranularity(
                    start = Instant.now(),
                    recordType = RecordType.SLEEP_TIME,
                    granularity = HealthDataGranularity.WEEKLY
                )

                _uiState.update { currentState ->
                    currentState.copy(
                        screenState = ScreenState.Success(),
                        selectedDate = dateForweek,
                        weeklySteps = stepsData,
                        weeklyCalories = caloriesData,
                        weeklyDistance = distanceData,
                        weeklyHeartRate = heartRateData,
                        weeklySleep = sleepData
                    )
                }
                    Log.d("ddd", "steps${stepsData}")
                    Log.d("ddd", "calories${caloriesData}")
                    Log.d("ddd", "distance${distanceData}")
                    Log.d("ddd", "heart_rate${heartRateData}")
                    Log.d("ddd", "sleep${sleepData}")
                } catch (e: Exception) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            screenState = ScreenState.Error(message = "data error: ${e.localizedMessage}")
                        )
                    }
                }
            }
        }
}
