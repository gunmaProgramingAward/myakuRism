package com.example.myaku_rismu.feature.musicDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.domain.model.MusicPlayerError
import com.example.myaku_rismu.domain.model.MusicPlayerState
import com.example.myaku_rismu.domain.useCase.MusicPlayerUseCase
import com.example.myaku_rismu.domain.useCase.SunoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

var taskID: String = "df3be84d5ec70b598f476f127167ed06"

@HiltViewModel
class MusicDetailViewModel @Inject constructor(
    private val sunoUseCase: SunoUseCase,
    private val musicPlayerUseCase: MusicPlayerUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MusicDetailState())
    val uiState: StateFlow<MusicDetailState> = _uiState.asStateFlow()
    val playerState: StateFlow<MusicPlayerState> =
        musicPlayerUseCase.observePlayerState()
            .map { result ->
                result.getOrElse { 
                    MusicPlayerState(
                        error = it as? MusicPlayerError
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = MusicPlayerState()
            )

    init {
        viewModelScope.launch {
            loadAndPlayMusic()
            _uiState.update {
                it.copy(
                    screenState = ScreenState.Success(true)
                )
            }
        }
    }

    private suspend fun loadAndPlayMusic() {
        try {
            val response = sunoUseCase.getMusic(taskId = taskID)
            val testTrack = response.taskDetail.response ?: return

            musicPlayerUseCase.loadTrack(testTrack)
        } catch (e: Exception) {
            _uiState.update {
                it.copy(screenState = ScreenState.Error(message = e.message))
            }
        }
    }

    fun togglePlayPause() {
        viewModelScope.launch {
            musicPlayerUseCase.togglePlayPause()
        }
    }

    fun seekToPosition(position: Float) {
        viewModelScope.launch {
            musicPlayerUseCase.seekToPercentage(position)
        }
    }

    fun toggleRepeatMode() {
        viewModelScope.launch {
            musicPlayerUseCase.toggleRepeatMode()
        }
    }

    fun reloadMusic() {
        viewModelScope.launch {
            loadAndPlayMusic()
        }
    }

    fun changeIsFavorite(boolean: Boolean) {
        _uiState.update {
            it.copy(
                isFavorite = boolean
            )
        }
    }

    fun changePlayerState(playerState: PlayerState) {
        _uiState.update {
            it.copy(
                playerState = playerState
            )
        }
    }

    fun changeMusicImagePausedRotation(rotation: Float) {
        _uiState.update {
            it.copy(
                musicImagePausedRotation = it.musicImagePausedRotation + rotation
            )
        }
    }

    fun changeDragOffset(offset: Float) {
        _uiState.update {
            it.copy(
                dragOffset = offset
            )
        }
    }

    fun plusDragOffset(offset: Float) {
        _uiState.update {
            it.copy(
                dragOffset = it.dragOffset + offset
            )
        }
    }
}