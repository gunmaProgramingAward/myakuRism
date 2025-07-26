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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.core.ui.TitleAndSubComponent
import com.example.myaku_rismu.core.ui.TopBar
import com.example.myaku_rismu.data.model.RecordType
import com.example.myaku_rismu.feature.healthDetail.components.PeriodTabList
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
    viewModel: HealthDetailViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
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

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(uiState.titleResId),
                navigationIcon = {
                    IconButton(onClick = { appState.navigatePopUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.top_bar_back_icon)
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
                onClickPeriod = { period ->
                    eventHandler(HealthDetailUiEvent.OnClickPeriod(period))
                },
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
    val periods = context.resources.getStringArray(R.array.health_detail_periods)
    val title = stringResource(uiState.titleResId)
    val healthTypeColor = uiState.color
    val healthTypeUnit = uiState.unitResId
    val graphTitleText = uiState.graphTitleText

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
            title = title,
            titleTextStyle = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            ),
            subComponent = {
                HealthMetric(
                    uiState = uiState,
                    healthTypeColor = healthTypeColor,
                    healthTypeUnit = stringResource(uiState.unitResId)
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
                    date = uiState.listDate,
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
    val recordType = uiState.recordType

    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
    ) {
        if (recordType != null) {
            Text(
                text = uiState.dailyAverage,
                color = healthTypeColor,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Text(
            text = stringResource(R.string.health_detail_move_slash),
            color = healthTypeColor,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = uiState.target.toString(),
            color = healthTypeColor,
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold
        )
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
    date: List<Long>,
    healthTypeColor: Color,
    modifier: Modifier = Modifier
) {
    val chartRenderData = rememberChartRenderData(uiState = uiState, data = date) ?: return

    LaunchedEffect(chartRenderData.fixedData) {
        chartRenderData.modelProducer.setEntries(
            date.mapIndexed { index, value ->
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