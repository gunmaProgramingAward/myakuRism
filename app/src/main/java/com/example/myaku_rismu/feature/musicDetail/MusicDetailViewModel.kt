package com.example.myaku_rismu.feature.musicDetail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MusicDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MusicDetailState())
    val uiState: StateFlow<MusicDetailState> = _uiState.asStateFlow()
}