package com.example.myaku_rismu.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myaku_rismu.R
import com.example.myaku_rismu.feature.home.components.BarChart
import com.example.myaku_rismu.feature.home.components.DonutChart
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme
import com.example.myaku_rismu.ui.theme.customTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    // --- 心拍数の表示値　---
    var bpmPlayerValue by remember { mutableIntStateOf(0) }
    // --- 各健康メトリックの値 ---
    var metricHeartRateCurrentValue by remember { mutableIntStateOf(0) }
    var metricStepsCurrentValue by remember { mutableIntStateOf(0) }
    var metricMoveCurrentValue by remember { mutableIntStateOf(0) }
    var metricSleepCurrentValue by remember { mutableIntStateOf(0) }
    var metricDistanceCurrentValue by remember { mutableIntStateOf(0) }
    // --- 各健康メトリックの目標値 ---
    var metricHeartRateTargetValue by remember { mutableIntStateOf(100) }
    var metricStepsTargetValue by remember { mutableIntStateOf(10000) }
    var metricMoveTargetValue by remember { mutableIntStateOf(2000) }
    var metricSleepTargetValue by remember { mutableIntStateOf(8) }
    var metricDistanceTargetValue by remember { mutableIntStateOf(5) }

    val bpmPlayerColor = when (bpmPlayerValue) {
        in 81..140 -> MaterialTheme.customTheme.homeMediumBpmColor
        in 140..300 -> MaterialTheme.customTheme.homeHighBpmColor
        else -> MaterialTheme.customTheme.homeLowBpmColor
    }

    // --- 各健康メトリックの色とバーの色(dataクラスにまとめるか検討中) ---
    val homeHeartRateColor = MaterialTheme.customTheme.homeHeartRateColor
    val homeStepsColor = MaterialTheme.customTheme.homeStepsColor
    val homeMoveColor = MaterialTheme.customTheme.homeMoveColor
    val homeSleepColor = MaterialTheme.customTheme.homeSleepColor
    val homeDistanceColor = MaterialTheme.customTheme.homeDistanceColor
    val homeHeartRateBarColorFaded = MaterialTheme.customTheme.homeHeartRateBarColorFaded
    val homeStepsBarColorFaded = MaterialTheme.customTheme.homeStepsBarColorFaded
    val homeMoveBarColorFaded = MaterialTheme.customTheme.homeMoveBarColorFaded
    val homeSleepBarColorFaded = MaterialTheme.customTheme.homeSleepBarColorFaded
    val homeDistanceBarColorFaded = MaterialTheme.customTheme.homeDistanceBarColorFaded


    val healthMetricsData = remember(
        metricHeartRateCurrentValue, metricHeartRateTargetValue,
        metricStepsCurrentValue, metricStepsTargetValue,
        metricMoveCurrentValue, metricMoveTargetValue,
        metricSleepCurrentValue, metricSleepTargetValue,
        metricDistanceCurrentValue, metricDistanceTargetValue
    ) {
        listOf(
            HealthMetric(
                titleResId = (R.string.current_heart_rate),
                iconResId = R.drawable.heartrate,
                cardThemeColor = homeHeartRateColor,
                barColorFaded = homeHeartRateBarColorFaded,
                currentValue = metricHeartRateCurrentValue,
                targetValue = metricHeartRateTargetValue,
                unitResId = R.string.unit_bpm
            ),
            HealthMetric(
                titleResId = (R.string.steps),
                iconResId = R.drawable.steps,
                cardThemeColor = homeStepsColor,
                barColorFaded = homeStepsBarColorFaded,
                currentValue = metricStepsCurrentValue,
                targetValue = metricStepsTargetValue,
                unitResId = null,
            ),
            HealthMetric(
                titleResId = (R.string.move),
                iconResId = R.drawable.move,
                cardThemeColor = homeMoveColor,
                barColorFaded = homeMoveBarColorFaded,
                currentValue = metricMoveCurrentValue,
                targetValue = metricMoveTargetValue,
                unitResId = R.string.unit_kcal,
            ),
            HealthMetric(
                titleResId = (R.string.sleep),
                iconResId = R.drawable.sleep,
                cardThemeColor = homeSleepColor,
                barColorFaded = homeSleepBarColorFaded,
                currentValue = metricSleepCurrentValue,
                targetValue = metricSleepTargetValue,
                unitResId = R.string.unit_hours,
            ),
            HealthMetric(
                titleResId = (R.string.distance),
                iconResId = R.drawable.distance,
                cardThemeColor = homeDistanceColor,
                barColorFaded = homeDistanceBarColorFaded,
                currentValue = metricDistanceCurrentValue,
                targetValue = metricDistanceTargetValue,
                unitResId = R.string.unit_km,
            )
        )
    }


// --- メインの画面レイアウト ---
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        BpmPlayerCard(
            modifier = Modifier,
            bpmCount = bpmPlayerValue,
            bpmColor = bpmPlayerColor
        )
    }
}


@Composable
fun BpmPlayerCard(
    modifier: Modifier = Modifier,
    bpmCount: Int,
    bpmColor: Color
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(310.dp)
            .background(bpmColor)
    ) {
        Image(
            painter = painterResource(id = R.drawable.shape),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$bpmCount",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 40.sp
                ),
                modifier = Modifier.padding(top = 14.dp)
            )
            Text(
                text = stringResource(R.string.bpm),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp
                ),
                modifier = Modifier.offset(y = (-8).dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /*音楽生成*/ },
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .height(36.dp)
                    .fillMaxWidth(0.3f),
                contentPadding = PaddingValues(vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.customTheme.settingScreenCardColor),
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.play),
                    color = MaterialTheme.customTheme.settingScreenTextColor,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Myaku_rismuTheme {
        HomeScreen()
    }
}


