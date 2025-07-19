package com.example.myaku_rismu.feature.profileDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.domain.model.ProfileDetailData
import com.example.myaku_rismu.domain.repository.ProfileDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val repository: ProfileDetailRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileDetailState())
    val uiState: StateFlow<ProfileDetailState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(screenState = ScreenState.Initializing())
        }
        viewModelScope.launch {
            try {
                val profileDetailData = repository.getProfileDetail()
                _uiState.update { profileDetailData.toState() }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(screenState = ScreenState.Error(
                        message = e.message ?: "エラーが発生しました")
                    )
                }
            }
        }
    }

    fun toggleSwitch(switchType: Int) {
        val enabled = when (switchType) {
            0 -> !_uiState.value.includeLyricsSwitchEnabled
            1 -> !_uiState.value.musicGenerationNotificationSwitchEnabled
            2 -> !_uiState.value.collaborationWithHealthcareSwitchEnabled
            3 -> !_uiState.value.syncWithYourSmartwatchSwitchEnabled
            else -> false
        }
        _uiState.update { currentState ->
            when (switchType) {
                0 -> currentState.copy(includeLyricsSwitchEnabled = enabled)
                1 -> currentState.copy(musicGenerationNotificationSwitchEnabled = enabled)
                2 -> currentState.copy(collaborationWithHealthcareSwitchEnabled = enabled)
                3 -> currentState.copy(syncWithYourSmartwatchSwitchEnabled = enabled)
                else -> currentState
            }
        }
        viewModelScope.launch {
            try {
                repository.updateSwitchState(switchType, enabled)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(screenState = ScreenState.Error(message = e.message ?: "エラーが発生しました"))
                }
            }
        }
    }
    private fun ProfileDetailData.toState(): ProfileDetailState {
        return ProfileDetailState(
            includeLyricsSwitchEnabled = this.includeLyricsSwitchEnabled,
            musicGenerationNotificationSwitchEnabled = this.musicGenerationNotificationSwitchEnabled,
            collaborationWithHealthcareSwitchEnabled = this.collaborationWithHealthcareSwitchEnabled,
            syncWithYourSmartwatchSwitchEnabled = this.syncWithYourSmartwatchSwitchEnabled
        )
    }
}