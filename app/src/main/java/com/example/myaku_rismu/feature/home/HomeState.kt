package com.example.myaku_rismu.feature.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.myaku_rismu.core.ScreenState

data class HomeState(
    val screenState: ScreenState = ScreenState.Initializing()
)

data class HealthMetric(
    @StringRes val titleResId: Int,
    @StringRes val unitResId: Int?,
    @DrawableRes val iconResId: Int,
    val cardThemeColor: Color,
    val barColorFaded: Color,
    val currentValue: Int,
    val targetValue: Int
) {
    val progress: Float
        get() = if (targetValue > 0) (currentValue.toFloat() / targetValue.toFloat()).coerceIn(0f, 1f) else 0f
}