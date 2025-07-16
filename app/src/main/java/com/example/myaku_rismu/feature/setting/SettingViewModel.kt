package com.example.myaku_rismu.feature.setting

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SettingState())
    val uiState: StateFlow<SettingState> = _uiState.asStateFlow()



    fun onEvent(event: SettingUiEvent) {
        when (event) {
            SettingUiEvent.ShowBirthdateDialog -> showDialog(SettingDialog.Birthdate)
            SettingUiEvent.ShowHeightDialog -> showDialog(SettingDialog.Height)
            SettingUiEvent.ShowWeightDialog -> showDialog(SettingDialog.Weight)
            SettingUiEvent.ShowGenderDialog -> showDialog(SettingDialog.Gender)
            SettingUiEvent.DismissDialog -> _uiState.update { it.copy(dialog = null) }
            is SettingUiEvent.BirthdateSelected -> _uiState.update {
                it.copy(
                    birthYear = event.year,
                    birthMonth = event.month,
                    birthDay = event.day,
                    dialog = null
                )
            }
            is SettingUiEvent.HeightSelected -> _uiState.update {
                it.copy(
                    heightCm = event.height,
                    dialog = null
                )
            }
            is SettingUiEvent.WeightSelected -> _uiState.update {
                it.copy(
                    weightKg = event.weight,
                    dialog = null
                )
            }
            is SettingUiEvent.GenderSelected -> _uiState.update {
                it.copy(
                    genderIndex = event.index,
                    dialog = null
                )
            }
            is SettingUiEvent.ActivityLevelSelected -> _uiState.update {
                it.copy(
                    activityLevelIndex = event.index
                )
            }
        }
    }

    private fun showDialog(dialog: SettingDialog) {
        _uiState.update { it.copy(dialog = dialog) }
    }
}

