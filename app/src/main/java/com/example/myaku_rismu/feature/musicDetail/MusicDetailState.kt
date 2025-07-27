package com.example.myaku_rismu.feature.musicDetail

import com.example.myaku_rismu.core.ScreenState

data class MusicDetailState(
    val screenState: ScreenState = ScreenState.Initializing(),
    val playerState: PlayerState = PlayerState.COLLAPSED,
    val musicSliderPositon: Float = 0f,
    val musicImagePausedRotation: Float = 0f,
    val dragOffset: Float = 0f,
    val isPlaying: Boolean = false,
    val isFavorite: Boolean = false,
    val isShuffle: Boolean = false,
    val isCreating: Boolean = true,
    val createProgress: Float = 0f
)

enum class PlayerState {
    COLLAPSED,
    EXPANDED
}

