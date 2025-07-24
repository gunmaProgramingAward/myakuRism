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
}