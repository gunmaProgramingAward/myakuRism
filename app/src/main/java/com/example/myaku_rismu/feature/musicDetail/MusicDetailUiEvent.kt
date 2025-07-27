package com.example.myaku_rismu.feature.musicDetail

import com.example.myaku_rismu.core.UiEvent

sealed interface MusicDetailUiEvent : UiEvent {
    data class ChangeIsPlaying(val boolean: Boolean) : MusicDetailUiEvent
    data class ChangeIsFavorite(val boolean: Boolean) : MusicDetailUiEvent
    data class ChangeIsShuffle(val boolean: Boolean) : MusicDetailUiEvent
    data class ChangePlayerState(val playerState: PlayerState) : MusicDetailUiEvent
    data class ChangeMusicSliderPosition(val position: Float) : MusicDetailUiEvent
    data class ChangeMusicImagePausedRotation(val rotation: Float) : MusicDetailUiEvent
    data class ChangeDragOffset(val offset: Float) : MusicDetailUiEvent
    data class PlusDragOffset(val offset: Float) : MusicDetailUiEvent
}