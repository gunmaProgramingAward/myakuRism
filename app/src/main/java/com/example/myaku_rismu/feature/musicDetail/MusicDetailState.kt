package com.example.myaku_rismu.feature.musicDetail

import com.example.myaku_rismu.core.ScreenState
import com.patrykandpatrick.vico.core.axis.Axis
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class MusicDetailState(
    val screenState: ScreenState = ScreenState.Initializing(),
    val stepData: List<Int> = emptyList(),
    val axisConfig: AxisConfig? = null,
    val selectedPeriods: Int = 0,
    val dailyAverage: String = "0",
) {

    val monthlyGraphTitle: String
        get() {
            val current = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy年M月")
            return current.format(formatter)
        }

    val yearlyGraphTitle: String
        get() {
            val current = LocalDate.now()
            return "${current.year}年"
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

