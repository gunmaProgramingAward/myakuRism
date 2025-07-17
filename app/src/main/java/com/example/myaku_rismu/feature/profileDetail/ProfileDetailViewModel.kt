package com.example.myaku_rismu.feature.profileDetail

import androidx.lifecycle.ViewModel
import com.example.myaku_rismu.core.ScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProfileDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileDetailState())
    val uiState: StateFlow<ProfileDetailState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(
                screenState = ScreenState.Initializing()
            )
        }
    }

    fun changeScreenState(newState: ScreenState) {
        _uiState.update { currentState ->
            currentState.copy(screenState = newState)
        }
    }

    fun toggleSwitch(switchType: Int) {
        _uiState.update { currentState ->
            when (switchType) {
                0 -> currentState.copy(includeLyricsSwitchEnabled = !currentState.includeLyricsSwitchEnabled)
                1 -> currentState.copy(musicGenerationNotificationSwitchEnabled = !currentState.musicGenerationNotificationSwitchEnabled)
                2 -> currentState.copy(collaborationWithHealthcareSwitchEnabled = !currentState.collaborationWithHealthcareSwitchEnabled)
                3 -> currentState.copy(syncWithYourSmartwatchSwitchEnabled = !currentState.syncWithYourSmartwatchSwitchEnabled)
                else -> currentState
            }
        }
    }
}