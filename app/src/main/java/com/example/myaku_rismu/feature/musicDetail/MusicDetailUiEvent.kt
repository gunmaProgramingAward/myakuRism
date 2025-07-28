package com.example.myaku_rismu.feature.musicDetail

import com.example.myaku_rismu.core.UiEvent

sealed interface MusicDetailUiEvent : UiEvent {
    data object TogglePlayPause : MusicDetailUiEvent
    data object ToggleRepeatMode : MusicDetailUiEvent
    data class SeekToPosition(val position: Float) : MusicDetailUiEvent
    data class ChangePlayerState(val playerState: PlayerState) : MusicDetailUiEvent
    data class ChangeFavoriteState(val isFavorite: Boolean) : MusicDetailUiEvent
    data class ChangeMusicImagePausedRotation(val rotation: Float) : MusicDetailUiEvent
    data class ChangeDragOffset(val offset: Float) : MusicDetailUiEvent
    data class PlusDragOffset(val offset: Float) : MusicDetailUiEvent
}