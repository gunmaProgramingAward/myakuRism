package com.example.myaku_rismu.feature.musicDetail

import com.example.myaku_rismu.core.ScreenState

data class MusicDetailState(
    val screenState: ScreenState = ScreenState.Initializing(),
    val playerState: PlayerState = PlayerState.COLLAPSED,
    val musicImagePausedRotation: Float = 0f,
    val dragOffset: Float = 0f,
    val isFavorite: Boolean = false,
    val isLoading: LoadingState = LoadingState.NOTTING,
    val isCreatedMusic: Boolean = false,
) {
    val loadingProgress: Float
        get() = when (isLoading) {
            LoadingState.NOTTING -> 0f
            LoadingState.PENDING -> 0.2f
            LoadingState.TEXT_SUCCESS -> 0.5f
            LoadingState.FIRST_SUCCESS -> 0.8f
            LoadingState.SUCCESS -> 1f
        }
}

enum class PlayerState {
    COLLAPSED,
    EXPANDED
}

enum class LoadingState(val state: String) {
    NOTTING("NOTHING"),
    PENDING("PENDING"),
    TEXT_SUCCESS("TEXT_SUCCESS"),
    FIRST_SUCCESS("FIRST_SUCCESS"),
    SUCCESS("SUCCESS");
    
    companion object {
        private val stateMap = entries.associateBy { it.state }
        
        fun fromStateString(state: String): LoadingState {
            return stateMap[state] ?: PENDING
        }
    }
}