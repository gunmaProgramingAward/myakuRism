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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myaku_rismu.R
import com.example.myaku_rismu.feature.home.HealthMetric
import com.example.myaku_rismu.feature.home.HealthMetricCardUi
import com.example.myaku_rismu.feature.home.HomeHealthType
import com.example.myaku_rismu.feature.home.HomeState
import com.example.myaku_rismu.ui.theme.customTheme
import kotlinx.coroutines.launch

// HomeBottomSheet.kt などに分離
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeBottomSheet(
    show: Boolean,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onHide: () -> Unit,
    onClick: (HealthMetric) -> Unit,
    onSwitchCheckedChange: (Boolean) -> Unit,
    uiState: HomeState,
    cardList: List<HealthMetricCardUi>
) {
    val scope = rememberCoroutineScope()
    if (show) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState
        ) {
            Box(Modifier.wrapContentSize()) {
                BottomSheetContent(
                    onHide = {
                        scope.launch { sheetState.hide() }
                            .invokeOnCompletion { onHide() }
                    },
                    onClick = onClick,
                    onSwitchCheckedChange = onSwitchCheckedChange,
                    uiState = uiState,
                    cardList = cardList
                )
            }
        }
    }
}


@Composable
fun BottomSheetContent(
    uiState: HomeState,
    cardList: List<HealthMetricCardUi>,
    onHide: () -> Unit,
    onClick: (HealthMetric) -> Unit,
    onSwitchCheckedChange: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.select_genre),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp)
        )
        HorizontalDivider(Modifier.padding(vertical = 18.dp))
        uiState.metrics.drop(1).chunked(2).forEachIndexed { rowIndex, rowItems ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                rowItems.forEachIndexed { colIndex, metric ->
                    val cardUi = cardList.getOrNull(rowIndex * 2 + colIndex + 1)
                    if (cardUi != null) {
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
        }
        Spacer(modifier = Modifier.padding(30.dp))
        Text(
            text = stringResource(R.string.choose_whether_to_include_lyrics),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp)
        )
        SwitchCard(
            modifier = Modifier.padding(16.dp),
            switchChecked = uiState.isSwitchChecked,
            onSwitchCheckedChange = onSwitchCheckedChange
        )
        Button(
            modifier = Modifier
                .padding(vertical = 38.dp)
                .defaultMinSize(minHeight = 50.dp),
            onClick = onHide,
            colors = ButtonDefaults.buttonColors(MaterialTheme.customTheme.settingScreenCardColor),
            elevation = ButtonDefaults.elevatedButtonElevation(4.dp),

            ) {
            Text(
                text = stringResource(R.string.create_music),
                color = MaterialTheme.customTheme.settingScreenTextColor,
                style = MaterialTheme.typography.headlineMedium
            )
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
    val backgroundColor = when {
        isSelected -> cardUi.color
        !enabled -> Color.LightGray
        else -> Color.White
    }
    val contentColor = when {
        isSelected -> Color.White
        !enabled -> Color.Gray
        else -> Color.Black
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(backgroundColor),
        elevation = CardDefaults.cardElevation(4.dp),
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
                painter = painterResource(cardUi.icon),
                contentDescription = stringResource(cardUi.title),
                tint = contentColor
            )
            Text(
                text = stringResource(cardUi.genre),
                style = MaterialTheme.typography.titleMedium,
                color = contentColor
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
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.outline_music_note_24),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(R.string.instrumental),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                modifier = Modifier,
                checked = switchChecked,
                onCheckedChange = onSwitchCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.customTheme.switchCheckedThumbColor,
                    uncheckedThumbColor = MaterialTheme.customTheme.switchUncheckedThumbColor,
                    checkedTrackColor = MaterialTheme.customTheme.switchCheckedTrackColor,
                    uncheckedTrackColor = MaterialTheme.customTheme.switchUncheckedTrackColor
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetContentPreview() {
    val cardList = listOf(
        HealthMetricCardUi(
            title = R.string.current_heart_rate,
            genre = R.string.unit_null,
            unit = R.string.bpm,
            icon = R.drawable.heartrate,
            color = MaterialTheme.customTheme.healthDetailMoveThemeColor,
            barColorFaded = MaterialTheme.customTheme.homeHeartRateBarColorFaded
        ),
        HealthMetricCardUi(
            title = R.string.steps,
            genre = R.string.edm,
            unit = R.string.unit_steps,
            icon = R.drawable.steps,
            color = MaterialTheme.customTheme.healthDetailWalkThemeColor,
            barColorFaded = MaterialTheme.customTheme.homeWalkBarColorFaded
        ),
        HealthMetricCardUi(
            title = R.string.move,
            genre = R.string.pops,
            unit = R.string.steps,
            icon = R.drawable.move,
            color = MaterialTheme.customTheme.healthDetailHeartRateThemeColor,
            barColorFaded = MaterialTheme.customTheme.homeMoveBarColorFaded
        ),
        HealthMetricCardUi(
            title = R.string.sleep,
            genre = R.string.classic,
            unit = R.string.unit_hours,
            icon = R.drawable.sleep,
            color = MaterialTheme.customTheme.healthDetailSleepThemeColor,
            barColorFaded = MaterialTheme.customTheme.homeSleepBarColorFaded
        ),
        HealthMetricCardUi(
            title = R.string.distance,
            genre = R.string.rock,
            unit = R.string.unit_km,
            icon = R.drawable.distance,
            color = MaterialTheme.customTheme.healthDetailMoveDistanceThemeColor,
            barColorFaded = MaterialTheme.customTheme.homeMoveDistanceBarColorFaded
        )
    )
    BottomSheetContent(
        uiState = HomeState(
            metrics = listOf(
                HealthMetric(HomeHealthType.HeartRate, 200, 180),
                HealthMetric(HomeHealthType.Move, 70, 180),
                HealthMetric(HomeHealthType.Walk, 10000, 10000),
                HealthMetric(HomeHealthType.MoveDistance, 1200, 2000),
                HealthMetric(HomeHealthType.SleepTime, 6, 8),
            ),
            selectedGenre = null,
            isSwitchChecked = false
        ),
        onHide = {},
        onClick = {},
        onSwitchCheckedChange = {},
        cardList = cardList
    )
}