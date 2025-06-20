package com.example.myaku_rismu.feature.musicDetail

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.ui.TitleAndSubComponent
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.layout.fullWidth
import com.patrykandpatrick.vico.core.axis.Axis
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.text.TextComponent
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import kotlin.math.roundToInt

@Composable
fun MusicDetailScreen(
    viewModel: MusicDetailViewModel
) {
    var stepData by remember { mutableStateOf(List(24) { (0..3000).random() }) }
    val periods = listOf("日", "週", "月", "年")
    var selectedPeriod by remember { mutableIntStateOf(0) }

    LaunchedEffect(selectedPeriod) {
        stepData = when (selectedPeriod) {
            // TODO: 仮の値なので後に書き換える
            0 -> List(24) { (500..10000).random() }
            1 -> List(7) { (500..10000).random() }
            2 -> List(31) { (500..10000).random() }
            3 -> List(12) { (500..10000).random() }
            else -> emptyList()
        }
    }

    MusicDetail(
        periods = periods,
        stepData = stepData,
        selectedPeriod = selectedPeriod,
        onPeriodSelected = { newPeriod ->
            selectedPeriod = newPeriod
        },
    )
}

@Composable
fun MusicDetail(
    periods: List<String>,
    stepData: List<Int>,
    selectedPeriod: Int,
    onPeriodSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        PeriodTabList(
            periods = periods,
            selectedPeriod = selectedPeriod,
            onPeriodSelected = { onPeriodSelected(it) },
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        TitleAndSubComponent(
            title = stringResource(R.string.MusicDetailScreen_all),
            titleTextStyle = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            ),
            subComponent = {
                Row(
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "50",
                        color = Color(0xFFFF1F61),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "/",
                        color = Color(0xFFFF1F61),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "130",
                        color = Color(0xFFFF1F61),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "KCAL",
                        color = Color.Black,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
            },
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        TitleAndSubComponent(
            title = stringResource(R.string.MusicDetail_today),
            titleTextStyle = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            ),
            subComponent = {
                AdvancedFixedAxisBarChart(
                    data = stepData,
                    selectedPeriod = selectedPeriod,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            },
            modifier = Modifier
                .padding(top = 40.dp)
                .padding(horizontal = 20.dp)
        )
    }
}

@Composable
fun AdvancedFixedAxisBarChart(
    data: List<Int>,
    selectedPeriod: Int,
    modifier: Modifier = Modifier
) {
    val modelProducer = remember { ChartEntryModelProducer() }
    val axisConfig = remember(selectedPeriod) {
        when (selectedPeriod) {
            0 -> AxisConfig(
                maxValue = 24,
                labelFormatter = { "${it}時" },
                spacing = 4,
                totalLabels = 6
            )
            1 -> {
                val weekDays = listOf("日", "月", "火", "水", "木", "金", "土")
                AxisConfig(
                    maxValue = 7,
                    labelFormatter = { weekDays.getOrElse(it) { "" } },
                    spacing = 1,
                    totalLabels = 7
                )
            }
            2 -> AxisConfig(
                maxValue = 31,
                labelFormatter = { "${it + 1}日" },
                spacing = 5,
                totalLabels = 7
            )
            3 -> AxisConfig(
                maxValue = 12,
                labelFormatter = { "${it + 1}" },
                spacing = 1,
                totalLabels = 6
            )
            else -> AxisConfig(
                maxValue = data.size.coerceAtLeast(1),
                labelFormatter = { it.toString() },
                spacing = 1,
                totalLabels = data.size.coerceAtLeast(1)
            )
        }
    }
    val fixedData = remember(data, axisConfig) {
        List(axisConfig.maxValue) { index ->
            data.getOrNull(index) ?: 0
        }
    }
    val itemPlacer = AxisItemPlacer.Horizontal.default(
        spacing = axisConfig.spacing,
    )
    val bottomAxisValueFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { value, _ ->
        axisConfig.labelFormatter(value.toInt())
    }
    val startAxis = rememberStartAxis(
        guideline = LineComponent(
            color = Color.Gray.toArgb(),
        ),
        label = TextComponent.Builder()
            .build(),
        itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = 6),
        valueFormatter = { value, _ ->
            value.roundToInt().toString()
        }
    )
    val bottomAxis = rememberBottomAxis(
        guideline = LineComponent(
            color = Color.Gray.toArgb(),
            thicknessDp = 1f
        ),
        label = TextComponent.Builder().build(),
        valueFormatter = bottomAxisValueFormatter,
        itemPlacer = itemPlacer
    )


    LaunchedEffect(fixedData) {
        modelProducer.setEntries(
            fixedData.mapIndexed { index, value ->
                entryOf(index.toFloat(), value.toFloat())
            }
        )
    }

    Chart(
        chart = columnChart(
            columns = listOf(
                LineComponent(
                    color = (0xFFE91E63).toInt(),
                    thicknessDp = 6f
                )
            ),
            spacing = 3.dp,
        ),
        chartModelProducer = modelProducer,
        startAxis = startAxis,
        bottomAxis = bottomAxis,
        modifier = modifier,
        horizontalLayout = HorizontalLayout.fullWidth(
            scalableStartPadding = 8.dp,
            scalableEndPadding = 8.dp
        ),
        diffAnimationSpec = tween(durationMillis = 1000)
    )
}

@Composable
fun PeriodTabList(
    periods: List<String>,
    selectedPeriod: Int,
    onPeriodSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .offset(y = 4.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = false
                )
        )
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(
                    color = Color(0xFFE8E8E8),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            val tabWidth = maxWidth / periods.size
            val indicatorOffset by animateDpAsState(targetValue = tabWidth * selectedPeriod)

            Box(
                modifier = Modifier
                    .offset { IntOffset(indicatorOffset.toPx().roundToInt(), 0) }
                    .width(tabWidth)
                    .fillMaxHeight()
                    .background(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(6.dp)
                    )
            )
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                periods.forEachIndexed { index, period ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = { onPeriodSelected(index) }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = period,
                            color = if (selectedPeriod == index) Color.Black else Color.Gray,
                            fontWeight = if (selectedPeriod == index) FontWeight.Medium else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MusicDetailScreenPreview() {
    var stepData by remember { mutableStateOf(List(24) { (0..3000).random() }) }
    val periods = listOf("日", "週", "月", "年")
    var selectedPeriod by remember { mutableIntStateOf(0) }

    LaunchedEffect(selectedPeriod) {
        when (selectedPeriod) {
            // TODO: 仮の値なので後に書き換える
            0 -> stepData = List((1..24).random()) { (0..3000).random() }
            1 -> stepData = List((1..7).random()) { (500..10000).random() }
            2 -> stepData = List((1..31).random()) { (500..10000).random() }
            3 -> stepData = List((1..12).random()) { (500..10000).random() }
        }
    }

    MusicDetail(
        periods = periods,
        stepData = stepData,
        selectedPeriod = selectedPeriod,
        onPeriodSelected = { newPeriod ->
            selectedPeriod = newPeriod
        },
        modifier = Modifier.padding(top = 50.dp)
    )
}