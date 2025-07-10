package com.example.myaku_rismu.feature.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.ui.theme.customTheme

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

data class WaveState(
    val scale: Animatable<Float, AnimationVector1D>,
    val alpha: Animatable<Float, AnimationVector1D>
)

data class BpmData(
    val bpmPlayerValue: Int = 120, // BPMの値を保持するプロパティ
) {
    val beatIntervalMs: Float
        get() = if(bpmPlayerValue > 0) (60f / bpmPlayerValue) * 1000f else 0f
    val newRippleStartIntervalMs: Int
        get() = beatIntervalMs.toInt() * 4
    val bpmPlayerColor: Color
        @Composable
        get() = when (bpmPlayerValue) {
            in 81..140 -> MaterialTheme.customTheme.homeMediumBpmRippleColor
            in 140..300 -> MaterialTheme.customTheme.homeHighBpmRippleColor
            else -> MaterialTheme.customTheme.homeLowBpmRippleColor
    }
}