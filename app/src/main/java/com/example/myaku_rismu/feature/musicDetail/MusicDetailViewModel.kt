package com.example.myaku_rismu.feature.musicDetail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MusicDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MusicDetailState())
    val uiState: StateFlow<MusicDetailState> = _uiState.asStateFlow()

    fun changeIsPlaying(boolean: Boolean) {
        _uiState.update {
            it.copy(
                isPlaying = boolean
            )
        }
    }

    fun changeIsFavorite(boolean: Boolean) {
        _uiState.update {
            it.copy(
                isFavorite = boolean
            )
        }
    }

    fun changeIsShuffle(boolean: Boolean) {
        _uiState.update {
            it.copy(
                isShuffle = boolean
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

    fun changeMusicSliderPosition(position: Float) {
        _uiState.update {
            it.copy(
                musicSliderPositon = position
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

    fun changeIsCreating(boolean: Boolean) {
        _uiState.update {
            it.copy(
                isCreating = boolean
            )
        }
    }
}