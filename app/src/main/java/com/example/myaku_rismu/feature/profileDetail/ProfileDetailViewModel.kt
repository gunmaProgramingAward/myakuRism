package com.example.myaku_rismu.feature.profileDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.data.model.ProfileSwitchType
import com.example.myaku_rismu.domain.useCase.ProfileDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val profileDetailUseCase: ProfileDetailUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileDetailState())
    val uiState: StateFlow<ProfileDetailState> = _uiState.asStateFlow()

    init {
        _uiState.update {
            it.copy(screenState = ScreenState.Initializing())
        }
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(display = profileDetailUseCase.getProfileDetail()) }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        screenState = ScreenState.Error(
                            messageResId = R.string.error
                        )
                    )
                }
            }
        }
    }

    fun toggleSwitch(switchType: ProfileSwitchType) {
        val enabled = when (switchType) {
            ProfileSwitchType.INCLUDE_LYRICS ->
                !_uiState.value.display.includeLyricsSwitchEnabled

            ProfileSwitchType.MUSIC_GENERATION_NOTIFICATION ->
                !_uiState.value.display.musicGenerationNotificationSwitchEnabled

            ProfileSwitchType.COLLABORATION_WITH_HEALTHCARE ->
                !_uiState.value.display.collaborationWithHealthcareSwitchEnabled

            ProfileSwitchType.SYNC_WITH_YOUR_SMARTWATCH ->
                !_uiState.value.display.syncWithYourSmartwatchSwitchEnabled
        }
        _uiState.update { currentState ->
            currentState.copy(
                display = when (switchType) {
                    ProfileSwitchType.INCLUDE_LYRICS ->
                        currentState.display.copy(includeLyricsSwitchEnabled = enabled)

                    ProfileSwitchType.MUSIC_GENERATION_NOTIFICATION ->
                        currentState.display.copy(musicGenerationNotificationSwitchEnabled = enabled)

                    ProfileSwitchType.COLLABORATION_WITH_HEALTHCARE ->
                        currentState.display.copy(collaborationWithHealthcareSwitchEnabled = enabled)

                    ProfileSwitchType.SYNC_WITH_YOUR_SMARTWATCH ->
                        currentState.display.copy(syncWithYourSmartwatchSwitchEnabled = enabled)
                }
            )
        }
        viewModelScope.launch {
            try {
                profileDetailUseCase.updateSwitchState(switchType, enabled)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        screenState = ScreenState.Error(
                            messageResId = R.string.error
                        )
                    )
                }
            }
        }
    }
}