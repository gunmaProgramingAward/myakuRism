package com.example.myaku_rismu.feature.home

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.ui.theme.customTheme

data class HomeState(
    val screenState: ScreenState = ScreenState.Initializing(),
    val bpmPlayerValue: Int = 0,
    val metrics: List<HealthMetric> = emptyList(),
) {
    val bpmPlayerColor: Color
    @Composable
    get() = when (bpmPlayerValue) {
        in 81..140 -> MaterialTheme.customTheme.homeMediumBpmColor
        in 140..300 -> MaterialTheme.customTheme.homeHighBpmColor
        else -> MaterialTheme.customTheme.homeLowBpmColor
    }
}

data class HealthMetric(
    val type: HomeHealthType,
    val currentValue: Int,
    val targetValue: Int
) {
    val progress: Float
        get() = if (targetValue > 0) (currentValue.toFloat() / targetValue).coerceIn(0f, 1f) else 0f

    val titleResId: Int get() = type.titleResId
    val unitResId: Int get() = type.unitResId
    val iconResId: Int get() = type.iconResId
    val cardThemeColor: Color @Composable get() = type.color
    val barColorFaded: Color @Composable get() = type.barColorFaded
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

sealed interface HomeHealthType {
    data object Move: HomeHealthType
    data object MoveDistance: HomeHealthType
    data object HeartRate: HomeHealthType
    data object SleepTime: HomeHealthType
    data object Walk: HomeHealthType
}

val HomeHealthType.titleResId: Int
    get() = when (this) {
        is HomeHealthType.Move -> R.string.move
        is HomeHealthType.MoveDistance -> R.string.health_detail_title_move_distance
        is HomeHealthType.HeartRate -> R.string.current_heart_rate
        is HomeHealthType.SleepTime -> R.string.health_detail_title_sleep
        is HomeHealthType.Walk -> R.string.health_detail_title_walk
    }
val HomeHealthType.unitResId: Int
    get() = when (this) {
        is HomeHealthType.Move -> R.string.health_detail_move_unit
        is HomeHealthType.MoveDistance -> R.string.health_detail_move_distance_unit
        is HomeHealthType.HeartRate -> R.string.health_detail_heart_rate_unit
        is HomeHealthType.SleepTime -> R.string.health_detail_sleep_time_unit
        is HomeHealthType.Walk -> R.string.health_detail_walk_unit
    }
val HomeHealthType.iconResId: Int
    @DrawableRes
    get() = when (this) {
        is HomeHealthType.Move -> R.drawable.move
        is HomeHealthType.MoveDistance -> R.drawable.distance
        is HomeHealthType.HeartRate -> R.drawable.heartrate
        is HomeHealthType.SleepTime -> R.drawable.sleep
        is HomeHealthType.Walk -> R.drawable.steps
    }
val HomeHealthType.color: Color
    @Composable
    get() = when (this) {
        is HomeHealthType.Move -> MaterialTheme.customTheme.homeMoveColor
        is HomeHealthType.MoveDistance -> MaterialTheme.customTheme.homeDistanceColor
        is HomeHealthType.HeartRate -> MaterialTheme.customTheme.homeHeartRateColor
        is HomeHealthType.SleepTime -> MaterialTheme.customTheme.homeSleepColor
        is HomeHealthType.Walk -> MaterialTheme.customTheme.homeStepsColor
    }
val HomeHealthType.barColorFaded: Color
    @Composable
    get() = when (this) {
        is HomeHealthType.Move -> MaterialTheme.customTheme.homeMoveBarColorFaded
        is HomeHealthType.MoveDistance -> MaterialTheme.customTheme.homeDistanceBarColorFaded
        is HomeHealthType.HeartRate -> MaterialTheme.customTheme.homeHeartRateBarColorFaded
        is HomeHealthType.SleepTime -> MaterialTheme.customTheme.homeSleepBarColorFaded
        is HomeHealthType.Walk -> MaterialTheme.customTheme.homeStepsBarColorFaded
    }