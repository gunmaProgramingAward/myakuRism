package com.example.myaku_rismu.feature.musicDetail

import com.example.myaku_rismu.core.ScreenState

data class MusicDetailState(
    val screenState: ScreenState = ScreenState.Initializing()
)

data class AxisConfig(
    val maxValue: Int,
    val labelFormatter: (Int) -> String,
    val spacing: Int,
    val totalLabels: Int
)

