package com.example.myaku_rismu.feature.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myaku_rismu.R
import com.example.myaku_rismu.data.model.RecordType
import com.example.myaku_rismu.feature.home.HealthMetric
import com.example.myaku_rismu.feature.home.HealthMetricCardUi
import com.example.myaku_rismu.feature.home.HomeState
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme
import com.example.myaku_rismu.ui.theme.customTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBottomSheet(
    show: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onCreate: () -> Unit,
    onClick: (HealthMetric) -> Unit,
    onSwitchCheckedChange: (Boolean) -> Unit,
    uiState: HomeState,
    cardList: List<HealthMetricCardUi>
) {
    if (show) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            containerColor = MaterialTheme.customTheme.settingScreenBackgroundColor,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.select_genre),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp)
                )
                HorizontalDivider(Modifier.padding(vertical = 18.dp))
                uiState.metrics.drop(1).chunked(2)
                    .zip(cardList.drop(1).chunked(2)).forEach { (rowItems, cardRowItems) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            rowItems.zip(cardRowItems).forEach { (metric, cardUi) ->
                                MusicGenreButton(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp),
                                    metric = metric,
                                    isSelected = (metric == uiState.selectedGenre),
                                    onClick = {
                                        if (metric.progress >= 1f)
                                            onClick(metric)
                                    },
                                    cardUi = cardUi
                                )
                            }
                        }
                    }
                Spacer(modifier = Modifier.size(41.dp))
                Text(
                    text = stringResource(R.string.choose_whether_to_include_lyrics),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp)
                )
                Spacer(modifier = Modifier.size(23.dp))
                SwitchCard(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    switchChecked = uiState.isSwitchChecked,
                    onSwitchCheckedChange = onSwitchCheckedChange
                )
                Spacer(modifier = Modifier.size(51.dp))
                Button(
                    modifier = Modifier
                        .padding(bottom = 37.dp)
                        .defaultMinSize(minHeight = 46.dp)
                        .width(164.dp),
                    onClick = {
                        onDismiss()
                        onCreate()
                    },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.customTheme.buttonBackgroundColor),
                    elevation = ButtonDefaults.elevatedButtonElevation(4.dp)
                ) {
                    Text(
                        text = stringResource(R.string.create_music),
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}


@Composable
fun MusicGenreButton(
    modifier: Modifier = Modifier,
    metric: HealthMetric,
    isSelected: Boolean,
    onClick: () -> Unit = { /* Default no-op */ },
    cardUi: HealthMetricCardUi
) {
    val enabled = metric.progress >= 1f

    Box(modifier = modifier) {
        Card(
            modifier = Modifier,
            colors = CardDefaults.cardColors(
                if (isSelected) cardUi.color
                else MaterialTheme.customTheme.myakuRismuCardColor
            ),
            elevation = if (isSelected) CardDefaults.cardElevation(4.dp)
            else CardDefaults.cardElevation(0.dp),
            onClick = { if (enabled) onClick() },
            enabled = enabled
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    modifier = Modifier.size(26.dp),
                    painter = painterResource(cardUi.icon),
                    contentDescription = stringResource(cardUi.title),
                    tint = if (isSelected) Color.White else cardUi.color,
                )
                Text(
                    text = stringResource(cardUi.genre),
                    style = MaterialTheme.typography.titleSmall,
                    color = if (isSelected) Color.White else Color.Black,
                )
            }
        }
        if (!enabled) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(MaterialTheme.customTheme.disabledBackgroundColor)
            )
        }
    }
}

@Composable
fun SwitchCard(
    modifier: Modifier = Modifier,
    switchChecked: Boolean = false,
    onSwitchCheckedChange: (Boolean) -> Unit = { /* Default no-op */ }
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.customTheme.myakuRismuCardColor)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.mic),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(R.string.instrumental),
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                modifier = Modifier,
                checked = switchChecked,
                onCheckedChange = onSwitchCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.customTheme.switchCheckedThumbColor,
                    uncheckedThumbColor = MaterialTheme.customTheme.switchUncheckedThumbColor,
                    checkedTrackColor = MaterialTheme.customTheme.bottomNavigationBarSelectedColor,
                    uncheckedTrackColor = MaterialTheme.customTheme.switchUncheckedTrackColor
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetContentPreview() {
    val uiState = HomeState(
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
            genre = R.string.edm,
            unit = R.string.unit_steps,
            icon = R.drawable.steps,
            color = MaterialTheme.customTheme.healthDetailWalkThemeColor,
            barColorFaded = MaterialTheme.customTheme.homeWalkBarColorFaded,
        ),
        HealthMetricCardUi(
            title = R.string.move,
            genre = R.string.hiphop,
            unit = R.string.steps,
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
            genre = R.string.pops,
            unit = R.string.unit_km,
            icon = R.drawable.distance,
            color = MaterialTheme.customTheme.healthDetailMoveDistanceThemeColor,
            barColorFaded = MaterialTheme.customTheme.homeMoveDistanceBarColorFaded,
        )
    )
    Myaku_rismuTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.select_genre),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
            )
            HorizontalDivider(Modifier.padding(vertical = 18.dp))
            uiState.metrics.drop(1).chunked(2)
                .zip(cardList.drop(1).chunked(2)).forEach { (rowItems, cardRowItems) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        rowItems.zip(cardRowItems).forEach { (metric, cardUi) ->
                            MusicGenreButton(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                metric = metric,
                                isSelected = (metric == uiState.selectedGenre),
                                onClick = {
                                },
                                cardUi = cardUi
                            )
                        }
                    }
                }
            Spacer(modifier = Modifier.size(41.dp))
            Text(
                text = stringResource(R.string.choose_whether_to_include_lyrics),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
            )
            Spacer(modifier = Modifier.size(23.dp))
            SwitchCard(
                modifier = Modifier.padding(horizontal = 16.dp),
                switchChecked = uiState.isSwitchChecked,
                onSwitchCheckedChange = {}
            )
            Spacer(modifier = Modifier.size(51.dp))
            Button(
                modifier = Modifier
                    .padding(bottom = 37.dp)
                    .defaultMinSize(minHeight = 46.dp)
                    .width(164.dp),
                onClick = {},
                colors = ButtonDefaults.buttonColors(MaterialTheme.customTheme.buttonBackgroundColor),
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
            ) {
                Text(
                    text = stringResource(R.string.create_music),
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}