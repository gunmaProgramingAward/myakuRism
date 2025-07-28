package com.example.myaku_rismu.feature.healthDetail

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.data.model.HealthDataGranularity
import com.example.myaku_rismu.data.model.RecordType
import com.example.myaku_rismu.ui.theme.customTheme
import com.patrykandpatrick.vico.core.axis.Axis
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class HealthDetailState(
    val screenState: ScreenState = ScreenState.Initializing(),
    val recordType: RecordType? = null,
    val listDate: List<Long> = emptyList(),
    val axisConfig: AxisConfig? = null,
    val selectedPeriods: Int = 0,
    val dailyAverage: Int = 0,
    val target: Int = 0,
    val isShowSettingDialog: Boolean = false,
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
        get() = when (selectedPeriods) {
            0 -> stringResource(R.string.health_detail_all)
            1 -> stringResource(R.string.health_detail_this_week)
            2 -> monthlyGraphTitle
            3 -> yearlyGraphTitle
            else -> stringResource(R.string.health_detail_daily_average)
        }

    val granularity: HealthDataGranularity
        get() = when (selectedPeriods) {
            0 -> HealthDataGranularity.HOURLY
            1 -> HealthDataGranularity.WEEKLY
            2 -> HealthDataGranularity.MONTHLY
            3 -> HealthDataGranularity.YEARLY
            else -> HealthDataGranularity.HOURLY
        }

    @get:StringRes
    val topBarTitleResId: Int
        get() = when (recordType) {
            RecordType.CALORIES -> R.string.health_detail_title_move
            RecordType.DISTANCE -> R.string.health_detail_title_move_distance
            RecordType.HEART_RATE -> R.string.health_detail_title_heart_rate
            RecordType.SLEEP_TIME -> R.string.health_detail_title_sleep
            RecordType.STEPS -> R.string.health_detail_title_walk
            null -> R.string.health_detail_title_move
        }

    @get:StringRes
    val titleResId: Int
        get() = when {
            recordType == RecordType.HEART_RATE ->
                R.string.health_detail_daily_average
            granularity == HealthDataGranularity.HOURLY ->
                R.string.health_detail_all
            else -> R.string.health_detail_daily_average
        }

    val color: Color
        @Composable
        get() = when (recordType) {
            RecordType.CALORIES -> MaterialTheme.customTheme.healthDetailMoveThemeColor
            RecordType.DISTANCE -> MaterialTheme.customTheme.healthDetailMoveDistanceThemeColor
            RecordType.HEART_RATE -> MaterialTheme.customTheme.healthDetailHeartRateThemeColor
            RecordType.SLEEP_TIME -> MaterialTheme.customTheme.healthDetailSleepThemeColor
            RecordType.STEPS -> MaterialTheme.customTheme.healthDetailWalkThemeColor
            null -> MaterialTheme.customTheme.healthDetailMoveThemeColor
        }

    @get:StringRes
    val unitResId: Int
        get() = when (recordType) {
            RecordType.CALORIES -> R.string.health_detail_move_unit
            RecordType.DISTANCE -> R.string.health_detail_move_distance_unit
            RecordType.HEART_RATE -> R.string.health_detail_heart_rate_unit
            RecordType.SLEEP_TIME -> R.string.health_detail_sleep_time_unit
            RecordType.STEPS -> R.string.health_detail_walk_unit
            null -> R.string.health_detail_move_unit
        }

    val targetOptions: List<String>
        get() = when (recordType) {
            RecordType.CALORIES -> (0..3000 step 10).map { it.toString() }
            RecordType.DISTANCE -> (0..10000 step 100).map { it.toString() }
            RecordType.HEART_RATE -> (0..200).map { it.toString() }
            RecordType.SLEEP_TIME -> (0..24).map { it.toString() }
            RecordType.STEPS -> (0..10000 step 50).map { it.toString() }
            null -> (0..30000 step 100).map { it.toString() }
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
    val fixedData: List<Long>
)

