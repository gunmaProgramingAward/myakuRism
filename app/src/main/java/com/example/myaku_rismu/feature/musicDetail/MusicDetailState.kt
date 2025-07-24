package com.example.myaku_rismu.feature.musicDetail

import com.example.myaku_rismu.core.ScreenState

data class MusicDetailState(
    val screenState: ScreenState = ScreenState.Initializing(),
    var isPlaying: Boolean = false,
    var isFavorite: Boolean = false,
)

