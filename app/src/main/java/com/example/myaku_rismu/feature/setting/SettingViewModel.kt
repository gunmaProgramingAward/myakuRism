package com.example.myaku_rismu.feature.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.data.model.SettingType
import com.example.myaku_rismu.domain.model.ActivityLevel
import com.example.myaku_rismu.domain.model.Gender
import com.example.myaku_rismu.domain.useCase.SettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingUseCase: SettingUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingState())
    val uiState: StateFlow<SettingState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(screenState = ScreenState.Initializing())
        }
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(display = settingUseCase.getSetting())}
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(screenState = ScreenState.Error(
                        messageResId = R.string.error)
                    )
                }
            }
        }
    }

    fun dismissDialog() {
        _uiState.update { it.copy(dialog = null) }
    }

    fun showDialog(dialog: SettingType) {
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
            settingUseCase.updateHeightAndWeight(SettingType.HEIGHT, height)
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
            settingUseCase.updateHeightAndWeight(SettingType.WEIGHT, weight)
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
            settingUseCase.updateBirthdate(SettingType.BIRTHDATE, year, month, day)
        }
    }

    fun selectGender(gender: Gender) {
        _uiState.update { state ->
            state.copy(
                display = state.display.copy(gender = gender),
                dialog = null
            )
        }
        viewModelScope.launch {
            settingUseCase.updateGender(gender)
        }
    }

    fun selectActivityLevel(level: ActivityLevel) {
        _uiState.update { state ->
            state.copy(
                display = state.display.copy(activityLevel = level)
            )
        }
        viewModelScope.launch {
            settingUseCase.updateActivityLevel(level)
        }
    }
}