package com.example.myaku_rismu.feature.home

import androidx.compose.animation.core.tween
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.data.model.RecordType
import com.example.myaku_rismu.core.ui.BarChart
import com.example.myaku_rismu.core.ui.DonutChart
import com.example.myaku_rismu.feature.home.components.GifImageLoader
import com.example.myaku_rismu.feature.home.components.HomeBottomSheet
import com.example.myaku_rismu.feature.home.components.LoopingRipple
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme
import com.example.myaku_rismu.ui.theme.customTheme
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    appState: AppState,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.updateMetrics()
    }

    fun eventHandler(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.ChangeBpmPlayerValue -> {
                viewModel.changeBpmPlayerValue(event.value)
            }

            is HomeUiEvent.SelectMusicGenre -> {
                viewModel.selectMusicGenre(event.metric)
            }

            is HomeUiEvent.OnSwitchCheckedChange -> {
                viewModel.onSwitchCheckedChange(event.isChecked)
            }

            is HomeUiEvent.DismissBottomSheet -> {
                viewModel.dismissBottomSheet()
            }

            is HomeUiEvent.ShowBottomSheet -> {
                viewModel.showBottomSheet()
            }

            is HomeUiEvent.CreateNewMusic -> {
                viewModel.createNewMusic(appState)
            }
        }
    }

    val cardList = listOf(
        HealthMetricCardUi(
            title = R.string.current_heart_rate,
            genre = R.string.unit_null,
            unit = R.string.bpm,
            icon = R.drawable.heartrate,
            color = MaterialTheme.customTheme.healthDetailMoveThemeColor,
            barColorFaded = MaterialTheme.customTheme.homeHeartRateBarColorFaded,
        ),
        HealthMetricCardUi(
            title = R.string.steps,
            genre = R.string.hiphop,
            unit = R.string.unit_steps,
            icon = R.drawable.steps,
            color = MaterialTheme.customTheme.healthDetailWalkThemeColor,
            barColorFaded = MaterialTheme.customTheme.homeWalkBarColorFaded,
        ),
        HealthMetricCardUi(
            title = R.string.move,
            genre = R.string.pops,
            unit = R.string.unit_kcal,
            icon = R.drawable.move,
            color = MaterialTheme.customTheme.healthDetailHeartRateThemeColor,
            barColorFaded = MaterialTheme.customTheme.homeMoveBarColorFaded,
        ),
        HealthMetricCardUi(
            title = R.string.sleep,
            genre = R.string.classic,
            unit = R.string.unit_hours,
            icon = R.drawable.sleep,
            color = MaterialTheme.customTheme.healthDetailSleepThemeColor,
            barColorFaded = MaterialTheme.customTheme.homeSleepBarColorFaded,
        ),
        HealthMetricCardUi(
            title = R.string.distance,
            genre = R.string.edm,
            unit = R.string.unit_km,
            icon = R.drawable.distance,
            color = MaterialTheme.customTheme.healthDetailMoveDistanceThemeColor,
            barColorFaded = MaterialTheme.customTheme.homeMoveDistanceBarColorFaded,
        )
    )

    Scaffold(modifier) { innerPadding ->
        HomeContent(
            uiState = uiState,
            appState = appState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            eventHandler = { event ->
                eventHandler(event)
            },
            cardList = cardList,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeState,
    appState: AppState,
    modifier: Modifier = Modifier,
    eventHandler: (HomeUiEvent) -> Unit,
    cardList: List<HealthMetricCardUi>,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    HomeBottomSheet(
        show = uiState.showBottomSheet,
        sheetState = sheetState,
        onDismiss = { eventHandler(HomeUiEvent.DismissBottomSheet) },
        onCreate = { eventHandler(HomeUiEvent.CreateNewMusic) },
        onClick = { metric -> eventHandler(HomeUiEvent.SelectMusicGenre(metric)) },
        onSwitchCheckedChange = { isChecked ->
            eventHandler(
                HomeUiEvent.OnSwitchCheckedChange(
                    isChecked
                )
            )
        },
        uiState = uiState,
        cardList = cardList
    )
    Column(modifier = modifier) {
        BpmPlayerCard(
            modifier = Modifier,
            showBottomSheet = {
                scope.launch { sheetState.show() }
                    .invokeOnCompletion { eventHandler(HomeUiEvent.ShowBottomSheet) }
            },
            uiState = uiState
        )
        Column(
            modifier = Modifier
                .background(MaterialTheme.customTheme.settingScreenBackgroundColor)
                .fillMaxSize()
        ) {
            HealthMetricsSection(
                uiState = uiState,
                cardList = cardList,
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 4.dp),
                onClick = { healthMetric ->
                    appState.navigateToHealthDetail(healthMetric.type)
                }
            )
        }
    }
}


@Composable
fun BpmPlayerCard(
    modifier: Modifier = Modifier,
    showBottomSheet: () -> Unit,
    uiState: HomeState
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(310.dp)
            .background(uiState.bpmPlayerColor)
    ) {
        LoopingRipple(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.Center),
            beatIntervalMs = uiState.beatIntervalMs,
            newRippleStartIntervalMs = uiState.newRippleStartIntervalMs,
            bpmPlayerRippleColor = uiState.bpmPlayerRippleColor
        )
        GifImageLoader(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.BottomCenter),
            gitResId = R.drawable.my_gif
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.bpmPlayerValue.toString(),
                style = MaterialTheme.typography.displayLarge,
                color = Color.White,
                modifier = Modifier.padding(top = 14.dp)
            )
            Text(
                text = stringResource(R.string.bpm),
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                modifier = Modifier.offset(y = (-8).dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { showBottomSheet() },
                enabled = !uiState.isCreatedMusic,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .height(36.dp)
                    .fillMaxWidth(0.3f),
                contentPadding = PaddingValues(vertical = 0.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.customTheme.buttonBackgroundColor,
                    disabledContainerColor = MaterialTheme.customTheme.buttonDisabledBackgroundColor
                ),
            ) {
                Text(
                    text = stringResource(R.string.create),
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}


@Composable
fun HealthMetricCard(
    modifier: Modifier = Modifier,
    metric: HealthMetric,
    cardUi: HealthMetricCardUi,
    onClick: () -> Unit = {}
) {
    LaunchedEffect(metric.progress) {
        metric.animatedProgress.snapTo(0f)
        metric.animatedProgress.animateTo(
            targetValue = metric.progress,
            animationSpec = tween(durationMillis = 1200)
        )
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(MaterialTheme.customTheme.myakuRismuCardColor),
        onClick = { onClick() },
    ) {
        if (cardUi.title == (R.string.current_heart_rate)) {
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
                        progress = metric.animatedProgress.value,
                        progressColor = cardUi.color,
                        barColorFaded = cardUi.barColorFaded,
                        modifier = Modifier
                    )
                    Icon(
                        painter = painterResource(id = cardUi.icon),
                        contentDescription = null,
                        tint = cardUi.color,
                    )
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(cardUi.title),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )
                    Row {
                        Text(
                            text = metric.currentValue.toString(),
                            style = MaterialTheme.typography.headlineLarge,
                            color = cardUi.color,
                            modifier = Modifier.alignByBaseline()
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = stringResource(cardUi.unit),
                            style = MaterialTheme.typography.titleMedium,
                            color = cardUi.color,
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
                    painter = painterResource(cardUi.icon),
                    contentDescription = stringResource(cardUi.title),
                    tint = cardUi.color,
                )
                Row(modifier = Modifier.offset(y = (-4).dp)) {
                    Text(
                        text = metric.currentValue.toString(),
                        style = MaterialTheme.typography.headlineLarge,
                        color = cardUi.color,
                        modifier = Modifier.alignByBaseline()
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = stringResource(cardUi.unit),
                        style = MaterialTheme.typography.headlineMedium,
                        color = cardUi.color,
                        modifier = Modifier.alignByBaseline()
                    )
                }
                Text(
                    text = stringResource(cardUi.title),
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Black,
                    modifier = Modifier.offset(y = (-8).dp)
                )
                BarChart(
                    modifier = Modifier.height(6.dp),
                    progress = metric.animatedProgress.value,
                    progressColor = cardUi.color,
                    barColorFaded = cardUi.barColorFaded,
                )
            }
        }
    }
}

@Composable
fun HealthMetricsSection(
    uiState: HomeState,
    cardList: List<HealthMetricCardUi>,
    modifier: Modifier = Modifier,
    onClick: (HealthMetric) -> Unit = {}
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        val firstMetric = uiState.metrics.getOrNull(0)
        val firstCardUi = cardList.getOrNull(0)

        if (firstMetric != null && firstCardUi != null) {
            HealthMetricCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                metric = firstMetric,
                cardUi = firstCardUi,
                onClick = { onClick(firstMetric) }
            )
        }

        if (uiState.metrics.size > 1) {
            uiState.metrics.drop(1).chunked(2).forEachIndexed { rowIndex, rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    rowItems.forEachIndexed { colIndex, metric ->
                        val cardUi = cardList.getOrNull(rowIndex * 2 + colIndex + 1)

                        if (cardUi != null) {
                            HealthMetricCard(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(100.dp),
                                metric = metric,
                                cardUi = cardUi,
                                onClick = { onClick(metric) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val dummyUiState = HomeState(
        metrics = listOf(
            HealthMetric(
                type = RecordType.HEART_RATE,
                currentValue = 200,
                targetValue = 180
            ),
            HealthMetric(
                type = RecordType.STEPS,
                currentValue = 5000,
                targetValue = 10000
            ),
            HealthMetric(
                type = RecordType.CALORIES,
                currentValue = 1200,
                targetValue = 2000
            ),
            HealthMetric(
                type = RecordType.SLEEP_TIME,
                currentValue = 9,
                targetValue = 8
            ),
            HealthMetric(
                type = RecordType.DISTANCE,
                currentValue = 2,
                targetValue = 5
            )
        )
    )
    val dummyCardList = listOf(
        HealthMetricCardUi(
            title = R.string.current_heart_rate,
            genre = R.string.unit_null,
            unit = R.string.bpm,
            icon = R.drawable.heartrate,
            color = MaterialTheme.customTheme.healthDetailMoveThemeColor,
            barColorFaded = MaterialTheme.customTheme.homeHeartRateBarColorFaded,
        ),
        HealthMetricCardUi(
            title = R.string.steps,
            genre = R.string.edm,
            unit = R.string.unit_steps,
            icon = R.drawable.steps,
            color = MaterialTheme.customTheme.healthDetailWalkThemeColor,
            barColorFaded = MaterialTheme.customTheme.homeWalkBarColorFaded,
        ),
        HealthMetricCardUi(
            title = R.string.move,
            genre = R.string.pops,
            unit = R.string.unit_kcal,
            icon = R.drawable.move,
            color = MaterialTheme.customTheme.healthDetailHeartRateThemeColor,
            barColorFaded = MaterialTheme.customTheme.homeMoveBarColorFaded,
        ),
        HealthMetricCardUi(
            title = R.string.sleep,
            genre = R.string.classic,
            unit = R.string.unit_hours,
            icon = R.drawable.sleep,
            color = MaterialTheme.customTheme.healthDetailSleepThemeColor,
            barColorFaded = MaterialTheme.customTheme.homeSleepBarColorFaded,
        ),
        HealthMetricCardUi(
            title = R.string.distance,
            genre = R.string.hiphop,
            unit = R.string.unit_km,
            icon = R.drawable.distance,
            color = MaterialTheme.customTheme.healthDetailMoveDistanceThemeColor,
            barColorFaded = MaterialTheme.customTheme.homeMoveDistanceBarColorFaded,
        )
    )

    Myaku_rismuTheme {
        val navHostController = NavHostController(LocalContext.current)
        HomeContent(
            uiState = dummyUiState,
            appState = AppState(navHostController),
            eventHandler = {},
            cardList = dummyCardList
        )
    }
}


