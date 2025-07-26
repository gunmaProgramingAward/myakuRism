package com.example.myaku_rismu.feature.healthDetail

import android.content.Context
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.core.ui.TitleAndSubComponent
import com.example.myaku_rismu.core.ui.TopBar
import com.example.myaku_rismu.feature.healthDetail.components.PeriodTabList
import com.example.myaku_rismu.ui.theme.customTheme
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.chart.layout.fullWidth
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.entry.entryOf

@Composable
fun HealthDetailScreen(
    appState: AppState,
    modifier: Modifier = Modifier,
    viewModel: HealthDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current


    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(uiState.topBarTitleResId),
                navigationIcon = {
                    IconButton(onClick = { appState.navigatePopUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.top_bar_back_icon)
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = { },
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.health_detail_target_setting),
                            style = MaterialTheme.typography.bodyLarge,
                            fontSize = 18.sp,
                            color = MaterialTheme.customTheme.musicDetailSettingTarget
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        if (uiState.screenState is ScreenState.Initializing) {
            Box(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.screenState is ScreenState.Success) {
            HealthDetail(
                uiState = uiState,
                onClickPeriod = viewModel::changeSelectedPeriod,
                context = context,
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
            )
        }
    }
}

@Composable
private fun HealthDetail(
    uiState: HealthDetailState,
    onClickPeriod: (Int) -> Unit,
    context: Context,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        PeriodTabList(
            periods = context.resources.getStringArray(R.array.health_detail_periods).toList(),
            selectedPeriod = uiState.selectedPeriods,
            onClickPeriod = onClickPeriod,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        TitleAndSubComponent(
            title = stringResource(uiState.titleResId),
            titleTextStyle = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            ),
            subComponent = {
                HealthMetric(
                    uiState = uiState,
                    healthTypeColor = uiState.color,
                    healthTypeUnit = stringResource(uiState.unitResId)
                )
            },
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        TitleAndSubComponent(
            title = uiState.graphTitleText,
            titleTextStyle = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            ),
            subComponent = {
                BarChart(
                    uiState = uiState,
                    date = uiState.listDate,
                    healthTypeColor = uiState.color,
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
private fun EmptyChartMessage(
    textMessage: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = textMessage,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
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
    val metricTextStyle = TextStyle(
        color = healthTypeColor,
        fontSize = 36.sp,
        fontWeight = FontWeight.Bold
    )

    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
    ) {
        Text(
            text = uiState.dailyAverage.toString(),
            style = metricTextStyle
        )
        Text(
            text = stringResource(R.string.health_detail_move_slash),
            style = metricTextStyle
        )
        Text(
            text = uiState.target.toString(),
            style = metricTextStyle
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = healthTypeUnit,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }
}

@Composable
private fun BarChart(
    uiState: HealthDetailState,
    date: List<Long>,
    healthTypeColor: Color,
    modifier: Modifier = Modifier
) {
    if (
        date.isEmpty() ||
        date.all { it == 0L } || 
        uiState.axisConfig == null || 
        uiState.screenState !is ScreenState.Success
        ) {
        EmptyChartMessage(
            textMessage = stringResource(R.string.health_detail_not_data_message),
            modifier = modifier
        )
        return
    }
    val chartRenderData = rememberChartRenderData(uiState = uiState, data = date)
    if (chartRenderData == null) {
        EmptyChartMessage(
            textMessage = stringResource(R.string.health_detail_not_reset),
            modifier = modifier
        )
        return
    }
    var isDataReady by remember { mutableStateOf(false) }

    LaunchedEffect(chartRenderData.fixedData) {
        if (chartRenderData.fixedData.isNotEmpty()) {
            chartRenderData.modelProducer.setEntries(
                chartRenderData.fixedData.mapIndexed { index, value ->
                    entryOf(index.toFloat(), value.toFloat())
                }
            )
            isDataReady = true
        }
    }

    if (!isDataReady) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = healthTypeColor)
        }
        return
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

@Preview
@Composable
fun HealthDetailScreenPreview() {
    val context = LocalContext.current

    HealthDetail(
        uiState = HealthDetailState(),
        onClickPeriod = {},
        context = context
    )
}