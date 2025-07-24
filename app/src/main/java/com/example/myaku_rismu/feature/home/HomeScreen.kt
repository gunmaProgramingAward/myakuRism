package com.example.myaku_rismu.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.feature.home.components.BarChart
import com.example.myaku_rismu.feature.home.components.DonutChart
import com.example.myaku_rismu.feature.home.components.GifImageLoader
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme
import com.example.myaku_rismu.ui.theme.customTheme
import com.example.myaku_rismu.feature.home.components.LoopingRipple


@Composable
fun HomeScreen(
    appState: AppState,
    viewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    fun eventHandler(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.changeBpmPlayerValue -> {
                viewModel.changeBpmPlayerValue(event.value)
            }
        }
    }

    Scaffold(modifier = modifier) { innerPadding ->
        HomeContent(
            uiState = uiState,
//            appState = appState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            eventHandler = { event ->
                eventHandler(event)
            }
        )
    }
}

@Composable
fun HomeContent(
    uiState: HomeState,
//    appState: AppState,
    modifier: Modifier = Modifier,
    eventHandler: (HomeUiEvent) -> Unit
) {
    Column(modifier = modifier) {
        BpmPlayerCard(
            modifier = Modifier,
            uiState = uiState
        )
        Column(
            modifier = Modifier.background(MaterialTheme.customTheme.settingScreenBackgroundColor)
        ) {
            HealthMetricsSection(
                uiState = uiState,
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 4.dp)
            )
        }
    }
}


@Composable
fun BpmPlayerCard(
    modifier: Modifier = Modifier,
    uiState: HomeState
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(310.dp)
            .background(uiState.bpmPlayerColor)
    ) {
        LoopingRipple(
            modifier = Modifier.align(Alignment.Center),
            beatIntervalMs = uiState.beatIntervalMs,
            newRippleStartIntervalMs = uiState.newRippleStartIntervalMs,
            bpmPlayerRippleColor = uiState.bpmPlayerRippleColor
        )
        GifImageLoader(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.BottomCenter)
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.bpmPlayerValue.toString(),
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(top = 14.dp)
            )
            Text(
                text = stringResource(R.string.bpm),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.offset(y = (-8).dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /*音楽生成*/ },
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .height(30.dp)
                    .fillMaxWidth(0.3f),
                contentPadding = PaddingValues(vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.customTheme.settingScreenCardColor),
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.play),
                    color = MaterialTheme.customTheme.settingScreenTextColor,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


@Composable
fun HealthMetricCard(
    modifier: Modifier = Modifier,
    metric: HealthMetric,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(MaterialTheme.customTheme.myakuRismuCardColor),
        onClick = { /*HealthDetailScreenに画面推移*/ },
    ) {
        if (metric.titleResId == (R.string.current_heart_rate)) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    DonutChart(
                        progress = metric.progress,
                        progressColor = metric.cardThemeColor,
                        barColorFaded = metric.barColorFaded,
                        modifier = Modifier
                    )
                    Icon(
                        painter = painterResource(id = metric.iconResId),
                        contentDescription = null,
                        tint = metric.cardThemeColor
                    )
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(metric.titleResId),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.customTheme.settingScreenTextColor
                    )
                    Row {
                        Text(
                            text = metric.currentValue.toString(),
                            style = MaterialTheme.typography.headlineLarge,
                            color = metric.cardThemeColor,
                            modifier = Modifier.alignByBaseline()
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = stringResource(metric.unitResId),
                            style = MaterialTheme.typography.titleMedium,
                            color = metric.cardThemeColor,
                            modifier = Modifier.alignByBaseline()
                        )
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 15.dp, bottom = 10.dp, start = 12.dp, end = 12.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(metric.iconResId),
                    contentDescription = stringResource(metric.titleResId),
                    tint = metric.cardThemeColor,
                )
                Row(modifier = Modifier.offset(y = (-4).dp)) {
                    Text(
                        text = metric.currentValue.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = metric.cardThemeColor,
                        modifier = Modifier.alignByBaseline()
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = stringResource(metric.unitResId),
                        style = MaterialTheme.typography.headlineMedium,
                        color = metric.cardThemeColor,
                        modifier = Modifier.alignByBaseline()
                    )
                }
                Text(
                    text = stringResource(metric.titleResId),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.customTheme.settingScreenTextColor,
                    modifier = Modifier.offset(y = (-8).dp)
                )
                BarChart(
                    progress = metric.progress,
                    progressColor = metric.cardThemeColor,
                    barColorFaded = metric.barColorFaded,
                )
            }
        }
    }
}


@Composable
fun HealthMetricsSection(
    uiState: HomeState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        HealthMetricCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            metric = uiState.metrics[0]
        )

        uiState.metrics.drop(1).chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                rowItems.forEach { metric ->
                    HealthMetricCard(
                        modifier = Modifier
                            .weight(1f)
                            .height(100.dp),
                        metric = metric
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    Myaku_rismuTheme {
        HomeContent(
            uiState = uiState,
//            appState = AppState(),
            eventHandler = {}
        )
    }
}


