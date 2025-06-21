package com.example.myaku_rismu.feature.healthDetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import kotlin.math.roundToInt

@Composable
fun rememberChartRenderData(uiState: HealthDetailState, data: List<Int>): ChartRenderData? {
    val axisConfig = uiState.axisConfig ?: return null

    val modelProducer = remember { ChartEntryModelProducer() }

    val fixedData = remember(data, axisConfig) {
        List(axisConfig.maxValue) { index ->
            data.getOrNull(index) ?: 0
        }
    }

    val bottomAxisValueFormatter = remember(axisConfig) {
        AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
            axisConfig.labelFormatter(value.toInt())
        }
    }

    val startAxis = rememberStartAxis(
        guideline = LineComponent(color = Color.Gray.toArgb()),
        label = TextComponent.Builder().build(),
        itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = 6),
        valueFormatter = { value, _ -> value.roundToInt().toString() }
    )

    val bottomAxis = rememberBottomAxis(
        guideline = LineComponent(color = Color.Gray.toArgb(), thicknessDp = 1f),
        label = TextComponent.Builder().build(),
        valueFormatter = bottomAxisValueFormatter,
        itemPlacer = AxisItemPlacer.Horizontal.default(spacing = axisConfig.spacing)
    )

    return remember(modelProducer, startAxis, bottomAxis, fixedData) {
        ChartRenderData(
            modelProducer = modelProducer,
            startAxis = startAxis,
            bottomAxis = bottomAxis,
            fixedData = fixedData
        )
    }
}