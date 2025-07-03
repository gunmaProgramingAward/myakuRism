package com.example.myaku_rismu.feature.healthDetail

import android.content.Context
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.ui.TitleAndSubComponent
import com.example.myaku_rismu.ui.theme.customTheme
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.layout.fullWidth
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.entry.entryOf
import kotlin.math.roundToInt

@Composable
fun HealthDetailScreen(
    appState: AppState,
    viewModel: HealthDetailViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    fun eventHandler(event: HealthDetailUiEvent) {
        when (event) {
            is HealthDetailUiEvent.OnClickPeriod -> {
                viewModel.changeSelectedPeriod(event.period)
            }
        }
    }

    HealthDetail(
        uiState = uiState,
        onClickPeriod = { newPeriod ->
            eventHandler(HealthDetailUiEvent.OnClickPeriod(newPeriod))
        },
        context = context,
        modifier = modifier
    )
}

@Composable
private fun HealthDetail(
    uiState: HealthDetailState,
    onClickPeriod: (Int) -> Unit,
    context: Context,
    modifier: Modifier = Modifier
) {
    val detailTitleText = when (uiState.selectedPeriods) {
        0 -> stringResource(R.string.health_detail_all)
        else -> stringResource(R.string.health_detail_daily_average)
    }
    val graphTitleText = when (uiState.selectedPeriods) {
        0 -> stringResource(R.string.health_detail_all)
        1 -> stringResource(R.string.health_detail_this_week)
        2 -> uiState.monthlyGraphTitle
        3 -> uiState.yearlyGraphTitle
        else -> stringResource(R.string.health_detail_daily_average)
    }
    val healthTypeColor =  when (uiState.healthType) {
        is HealthType.Move -> MaterialTheme.customTheme.healthDetailMoveThemeColor
        is HealthType.MoveDistance -> MaterialTheme.customTheme.healthDetailMoveDistanceThemeColor
        is HealthType.HeartRate -> MaterialTheme.customTheme.healthDetailHeartRateThemeColor
        is HealthType.SleepTime -> MaterialTheme.customTheme.healthDetailSleepThemeColor
        is HealthType.Walk -> MaterialTheme.customTheme.healthDetailWalkThemeColor
        null -> Color.Black
    }
    val healthTypeUnit = when (uiState.healthType) {
        is HealthType.Move -> stringResource(R.string.health_detail_move_unit)
        is HealthType.MoveDistance -> stringResource(R.string.health_detail_move_distance_unit)
        is HealthType.HeartRate -> stringResource(R.string.health_detail_heart_rate_unit)
        is HealthType.SleepTime -> stringResource(R.string.health_detail_sleep_time_unit)
        is HealthType.Walk -> stringResource(R.string.health_detail_walk_unit)
        null -> ""
    }
    val periods = context.resources.getStringArray(R.array.health_detail_periods)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        PeriodTabList(
            periods = periods.toList(),
            selectedPeriod = uiState.selectedPeriods,
            onClickPeriod = { onClickPeriod(it) },
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        TitleAndSubComponent(
            title = detailTitleText,
            titleTextStyle = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            ),
            subComponent = {
                HealthMetric(
                    uiState = uiState,
                    healthTypeColor = healthTypeColor,
                    healthTypeUnit = healthTypeUnit
                )
            },
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        TitleAndSubComponent(
            title = graphTitleText,
            titleTextStyle = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            ),
            subComponent = {
                BarChart(
                    uiState = uiState,
                    data = uiState.stepData,
                    healthTypeColor = healthTypeColor,
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
private fun HealthMetric(
    uiState: HealthDetailState,
    healthTypeColor: Color,
    healthTypeUnit: String,
    modifier: Modifier = Modifier
) {
    val healthType = uiState.healthType

    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
    ) {
        if (healthType != null) {
            Text(
                text = uiState.dailyAverage,
                color = healthTypeColor,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (healthType is HealthType.Move) {
        Text(
            text = stringResource(R.string.health_detail_move_slash),
            color = healthTypeColor,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = healthType.target.toString(),
                color = healthTypeColor,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = healthTypeUnit,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
    }
}

@Composable
private fun BarChart(
    uiState: HealthDetailState,
    data: List<Int>,
    healthTypeColor: Color,
    modifier: Modifier = Modifier
) {
    val chartRenderData = rememberChartRenderData(uiState = uiState, data = data) ?: return

    LaunchedEffect(chartRenderData.fixedData) {
        chartRenderData.modelProducer.setEntries(
            chartRenderData.fixedData.mapIndexed { index, value ->
                entryOf(index.toFloat(), value.toFloat())
            }
        )
    }

    Chart(
        chart = columnChart(
            columns = listOf(
                LineComponent(
                    color = healthTypeColor.toArgb(),
                    thicknessDp = 6f
                )
            ),
            spacing = 3.dp,
        ),
        chartModelProducer = chartRenderData.modelProducer,
        startAxis = chartRenderData.startAxis,
        bottomAxis = chartRenderData.bottomAxis,
        modifier = modifier,
        horizontalLayout = HorizontalLayout.fullWidth(
            scalableStartPadding = 8.dp,
            scalableEndPadding = 8.dp
        ),
        diffAnimationSpec = tween(durationMillis = 1000)
    )
}

@Composable
private fun PeriodTabList(
    periods: List<String>,
    selectedPeriod: Int,
    onClickPeriod: (Int) -> Unit,
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
                    color = MaterialTheme.customTheme.healthDetailSelectedPeriodTabColor,
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
                        color = MaterialTheme.customTheme.healthDetailSelectedPeriodTabColor,
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
                                onClick = { onClickPeriod(index) }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = period,
                            color = if (selectedPeriod == index) Color.Black else Color.Gray,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun HealthDetailScreenPreview() {
    val viewModel: HealthDetailViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    HealthDetail(
        uiState = uiState,
        onClickPeriod = { newPeriod ->
            viewModel.changeSelectedPeriod(newPeriod)
        },
        context = context
    )
}