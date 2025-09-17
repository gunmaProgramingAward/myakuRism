package com.example.myaku_rismu.feature.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.data.model.RecordType
import com.example.myaku_rismu.ui.theme.customTheme


data class HomeState(
    val screenState: ScreenState = ScreenState.Initializing,
    val bpmPlayerValue: Int = 85,
    val metrics: List<HealthMetric> = emptyList(),
    val selectedGenre: HealthMetric? = null,
    val isSwitchChecked: Boolean = false,
    val selectedBpmValue: Int? = null,
    val bpmValues: List<Int> = emptyList(),
    val showBottomSheet: Boolean = false,
    val isInstrumental: Boolean = false,
    val isEnabledCreateMusic: Boolean = false,
    val isMusicGenerating: Boolean = false,
) {
    val beatIntervalMs: Float
        get() {
            val millisPerMinute = 60_000f
            return if (bpmPlayerValue > 0) (millisPerMinute / bpmPlayerValue) else 0f
        }
    val newRippleStartIntervalMs: Int
        get() = beatIntervalMs.toInt() * 4

    val bpmPlayerRippleColor: Color
        @Composable
        get() {
            val bpmMediumMin = 81
            val bpmHighMin = 140
            val bpmMax = 300
            return when (bpmPlayerValue) {
                in bpmMediumMin until bpmHighMin -> MaterialTheme.customTheme.homeMediumBpmRippleColor
                in bpmHighMin..bpmMax -> MaterialTheme.customTheme.homeHighBpmRippleColor
                else -> MaterialTheme.customTheme.homeLowBpmRippleColor
            }
        }

    val bpmPlayerColor: Color
        @Composable
        get() = when (bpmPlayerValue) {
            in 81..140 -> MaterialTheme.customTheme.homeMediumBpmColor
            in 140..300 -> MaterialTheme.customTheme.homeHighBpmColor
            else -> MaterialTheme.customTheme.homeLowBpmColor
        }
}

data class HealthMetric(
    val type: RecordType,
    val currentValue: Int,
    val targetValue: Int,
    val animatedProgress: Animatable<Float, AnimationVector1D> = Animatable(0f),
) {
    val progress: Float
        get() = if (targetValue > 0) (currentValue.toFloat() / targetValue).coerceIn(0f, 1f) else 0f
}

data class WaveState(
    val scale: Animatable<Float, AnimationVector1D>,
    val alpha: Animatable<Float, AnimationVector1D>
)

data class HealthMetricCardUi(
    val title: Int,
    val genre: Int,
    val unit: Int,
    val icon: Int,
    val color: Color,
    val barColorFaded: Color,
)