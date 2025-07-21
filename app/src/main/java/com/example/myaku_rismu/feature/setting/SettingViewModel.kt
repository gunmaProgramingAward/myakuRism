package com.example.myaku_rismu.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.data.repository.SettingRepositoryImpl
import com.example.myaku_rismu.data.useCase.GetHeightUseCase
import com.example.myaku_rismu.data.useCase.GetWeightUseCase
import com.example.myaku_rismu.domain.model.SettingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getHeightUseCase: GetHeightUseCase,
    private val getWeightUseCase: GetWeightUseCase,
    private val repository: SettingRepositoryImpl
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingState())
    val uiState: StateFlow<SettingState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(screenState = ScreenState.Initializing())
        }
        viewModelScope.launch {
            try {
                val settingData = repository.getSetting()
                _uiState.update { settingData.toState() }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(screenState = ScreenState.Error(
                        message = e.message ?: "エラーが発生しました")
                    )
                }
            }
        }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(dialog = null) }
    }

    fun showDialog(dialog: SettingDialog) {
        _uiState.update { it.copy(dialog = dialog) }
    }

    fun selectHeight(height: Int) {
        _uiState.update { state ->
            state.copy(
                display = state.display.copy(heightCm = height),
                dialog = null
            )
        }
        viewModelScope.launch {
            repository.updateSettingState(0, height)
        }
    }

    fun selectWeight(weight: Int) {
        _uiState.update { state ->
            state.copy(
                display = state.display.copy(weightKg = weight),
                dialog = null
            )
        }
        viewModelScope.launch {
            repository.updateSettingState(1, weight)
        }
    }

    fun selectGender(index: Int) {
        _uiState.update { state ->
            state.copy(
                display = state.display.copy(gender = index),
                dialog = null
            )
        }
        viewModelScope.launch {
            repository.updateSettingState(2, index)
        }
    }

    fun selectBirthdate(year: Int, month: Int, day: Int) {
        _uiState.update { state ->
            state.copy(
                display = state.display.copy(
                    birthYear = year,
                    birthMonth = month,
                    birthDay = day
                ),
                dialog = null
            )
        }
        viewModelScope.launch {
            repository.updateSettingState(3, year, month, day)
        }
    }

    fun selectActivityLevel(index: Int) {
        _uiState.update { state ->
            state.copy(activityLevelIndex = index)
        }
    }

    suspend fun loadProfileData() {
        val height = getHeightUseCase()
        val weight = getWeightUseCase()
        _uiState.update { state ->
            state.copy(
                display = state.display.copy(
                    heightCm = height?.times(100)?.toInt()?: state.display.heightCm,
                    weightKg = weight?.toInt() ?: state.display.weightKg,
                )
            )
        }
    }

    private fun SettingData.toState(): SettingState {
        return SettingState(
            display = ProfileData(
                heightCm = this.heightCm,
                weightKg = this.weightKg,
                gender = this.gender,
                birthYear = this.birthYear,
                birthMonth = this.birthMonth,
                birthDay = this.birthDay
            )
        )
    }
}