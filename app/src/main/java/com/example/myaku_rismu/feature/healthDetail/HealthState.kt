package com.example.myaku_rismu.feature.healthDetail

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.ui.theme.customTheme
import com.patrykandpatrick.vico.core.axis.Axis
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class HealthDetailState(
    val screenState: ScreenState = ScreenState.Initializing(),
    val healthType: HealthType? = null,
    val stepData: List<Int> = emptyList(),
    val axisConfig: AxisConfig? = null,
    val selectedPeriods: Int = 0,
    val dailyAverage: String = "0",
) {

    private val monthlyGraphTitle: String
        get() {
            val current = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy年M月")
            return current.format(formatter)
        }

    private val yearlyGraphTitle: String
        get() {
            val current = LocalDate.now()
            return "${current.year}年"
        }

    val graphTitleText: String
        @Composable
        get() = when(selectedPeriods) {
            0 -> stringResource(R.string.health_detail_all)
            1 -> stringResource(R.string.health_detail_this_week)
            2 -> monthlyGraphTitle
            3 -> yearlyGraphTitle
            else -> stringResource(R.string.health_detail_daily_average)
        }
}

data class AxisConfig(
    val maxValue: Int,
    val labelFormatter: (Int) -> String,
    val spacing: Int,
    val totalLabels: Int
)

data class ChartRenderData(
    val modelProducer: ChartEntryModelProducer,
    val startAxis: Axis<AxisPosition.Vertical.Start>,
    val bottomAxis: Axis<AxisPosition.Horizontal.Bottom>,
    val fixedData: List<Int>
)

sealed interface HealthType {
    @get:StringRes
    val titleResId: Int
        get() = when (this) {
            is Move -> R.string.health_detail_title_move
            is MoveDistance -> R.string.health_detail_title_move_distance
            is HeartRate -> R.string.health_detail_title_heart_rate
            is SleepTime -> R.string.health_detail_title_sleep
            is Walk -> R.string.health_detail_title_walk
        }

    val color: Color
        @Composable
        get() = when (this) {
            is Move -> MaterialTheme.customTheme.healthDetailMoveThemeColor
            is MoveDistance -> MaterialTheme.customTheme.healthDetailMoveDistanceThemeColor
            is HeartRate -> MaterialTheme.customTheme.healthDetailHeartRateThemeColor
            is SleepTime -> MaterialTheme.customTheme.healthDetailSleepThemeColor
            is Walk -> MaterialTheme.customTheme.healthDetailWalkThemeColor
        }

    @get:StringRes
    val unitResId: Int
        get() = when (this) {
            is Move -> R.string.health_detail_move_unit
            is MoveDistance -> R.string.health_detail_move_distance_unit
            is HeartRate -> R.string.health_detail_heart_rate_unit
            is SleepTime -> R.string.health_detail_sleep_time_unit
            is Walk -> R.string.health_detail_walk_unit
        }

    data class Move(val target: Int) : HealthType
    data object MoveDistance : HealthType
    data object HeartRate : HealthType
    data object SleepTime : HealthType
    data object Walk : HealthType
}
