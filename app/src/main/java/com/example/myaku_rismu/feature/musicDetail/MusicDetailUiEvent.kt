package com.example.myaku_rismu.feature.musicDetail

import com.example.myaku_rismu.core.UiEvent

sealed interface MusicDetailUiEvent : UiEvent {
    data class ChangeIsPlaying(val boolean: Boolean) : MusicDetailUiEvent
    data class ChangeIsFavorite(val boolean: Boolean) : MusicDetailUiEvent
}