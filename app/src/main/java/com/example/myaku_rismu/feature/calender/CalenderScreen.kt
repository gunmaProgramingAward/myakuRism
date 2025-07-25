package com.example.myaku_rismu.feature.calender

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import androidx.compose.ui.res.stringResource
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.AppState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.padding
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myaku_rismu.feature.home.CalenderViewModel
import androidx.annotation.DrawableRes
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.draw.blur

enum class HealthMetricType(
    @DrawableRes val iconResId: Int
) {
    MOVE(R.drawable.move),
    HEART_RATE(R.drawable.heart),
    SLEEP(R.drawable.sleep),
    STEPS(R.drawable.walk),
    WALK_DISTANCE(R.drawable.distance)
}

data class HealthData(
    val type: HealthMetricType,
    val name: String,
    val primaryColor: Color,
    val unit: String,
    val currentValue: Float,
    val goalValue: Float
) {
    val progress: Float
        get() = (currentValue / goalValue).coerceIn(0f, 1f)
}

data class DailyHealthReport(
    val move: HealthData,
    val heartRate: HealthData,
    val sleep: HealthData,
    val steps: HealthData,
    val walkDistance: HealthData
) {
    fun getByType(type: HealthMetricType): HealthData = when (type) {
        HealthMetricType.MOVE -> move
        HealthMetricType.HEART_RATE -> heartRate
        HealthMetricType.SLEEP -> sleep
        HealthMetricType.STEPS -> steps
        HealthMetricType.WALK_DISTANCE -> walkDistance
    }
}

@Composable
fun CalenderScreen(
    appState: AppState,
    modifier: Modifier = Modifier,
    viewModel: CalenderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val eventHandler = remember(viewModel) {
        { event: CalenderUiEvent ->
            viewModel.onEvent(event)
        }
    }

    var currentDisplayDate by remember(uiState.selectedDate) { mutableStateOf(uiState.selectedDate) }
    if (uiState.isLoading && uiState.weeklySteps.all { it == 0L }) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    uiState.error?.let { errorMsg ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("오류: $errorMsg", color = MaterialTheme.colorScheme.error)
        }
    }

    val healthDataByDateFromState: Map<LocalDate, DailyHealthReport> =
        convertCalenderStateToHealthDataMap(
            uiState,
            currentDisplayDate
        )
    HealthDashboardScreen(
        modifier = modifier,
        isLoading = uiState.isLoading,
        currentDate = currentDisplayDate,
        healthReportsForWeek = healthDataByDateFromState,
        onDateSelected = { selectedDate ->
            currentDisplayDate = selectedDate
            eventHandler(CalenderUiEvent.OnDateSelected(selectedDate))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthDashboardScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    currentDate: LocalDate,
    healthReportsForWeek: Map<LocalDate, DailyHealthReport>,
    onDateSelected: (LocalDate) -> Unit
) {
    var selectedMetricTypeState by remember { mutableStateOf(HealthMetricType.MOVE) }
    val dailyReport = healthReportsForWeek[currentDate]
        ?: createEmptyDailyReport(currentDate)

    val dateFormatPattern = stringResource(id = R.string.calender_screen_date)
    val dateFormatter = remember(dateFormatPattern) {
        DateTimeFormatter.ofPattern(dateFormatPattern, Locale.JAPAN)
    }
    val selectedData = dailyReport.getByType(selectedMetricTypeState)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "Calender",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                ) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            WeeklyCalendar(
                selectedDate = currentDate,
                onDateSelected = onDateSelected,
                healthReports = healthReportsForWeek
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = currentDate.format(dateFormatter),
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            CircularHealthDashboard(
                report = dailyReport,
                selectedMetricType = selectedMetricTypeState,
                onMetricSelected = { type ->
                    selectedMetricTypeState = type
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            )

            HealthDetailText(data = selectedData)

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}



@Composable
fun WeeklyCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    healthReports: Map<LocalDate, DailyHealthReport>
) {
    var displayDate by remember(selectedDate) { mutableStateOf(selectedDate) }
    val weekStart = displayDate.minusDays(3)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { displayDate = displayDate.minusDays(1) }) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = stringResource(id = R.string.calender_screen_previous))
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (i in 0..6) {
                val date = weekStart.plusDays(i.toLong())
                val hasData = healthReports.containsKey(date)
                val report = healthReports[date]
                val progress = report?.move?.progress ?: 0f
                val color = report?.move?.primaryColor ?: Color.LightGray

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable(enabled = hasData) { onDateSelected(date) }
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        fontSize = 14.sp,
                        color = if (hasData) LocalContentColor.current else Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    HealthRing(
                        progress = progress,
                        color = color,
                        size = 32.dp,
                        strokeWidth = 3.dp,
                        isSelected = (date == selectedDate)
                    )
                }
            }
        }

        IconButton(onClick = { displayDate = displayDate.plusDays(1) }) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.calender_screen_next))
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CircularHealthDashboard(
    report: DailyHealthReport,
    selectedMetricType: HealthMetricType,
    onMetricSelected: (HealthMetricType) -> Unit,
    modifier: Modifier = Modifier
) {
    val mainData = report.getByType(selectedMetricType)

    val animatedProgress by animateFloatAsState(
        targetValue = mainData.progress,
        animationSpec = tween(durationMillis = 1000),
        label = stringResource(id = R.string.calender_screen_animation)
    )

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val dashboardSize = minOf(maxWidth, maxHeight) * 0.95f

        HealthRing(
            progress = animatedProgress,
            color = mainData.primaryColor,
            size = dashboardSize,
            strokeWidth = dashboardSize * 0.07f
        )

        val radius = dashboardSize * 0.23f
        val smallRingSize = dashboardSize * 0.25f

        val slots = listOf(
            Pair(-Math.PI / 2, HealthMetricType.HEART_RATE),
            Pair(0.0, HealthMetricType.STEPS),
            Pair(Math.PI / 2, HealthMetricType.WALK_DISTANCE),
            Pair(Math.PI, HealthMetricType.SLEEP)
        )

        slots.forEach { (angle, defaultTypeInSlot) ->
            val typeToShowInSlot = when {
                selectedMetricType != HealthMetricType.MOVE && defaultTypeInSlot == selectedMetricType -> {
                    HealthMetricType.MOVE
                }
                else -> defaultTypeInSlot
            }
            val data = report.getByType(typeToShowInSlot)

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset {
                        val x = (radius.toPx() * cos(angle)).roundToInt()
                        val y = (radius.toPx() * sin(angle)).roundToInt()
                        IntOffset(x, y)
                    }
                    .size(smallRingSize)
                    .clickable { onMetricSelected(typeToShowInSlot) }
            ) {
                HealthRing(
                    progress = data.progress,
                    color = data.primaryColor,
                    size = smallRingSize,
                    strokeWidth = smallRingSize * 0.1f,
                )
                Box(
                    modifier = Modifier.align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    val yShadow = if (data.type == HealthMetricType.MOVE) 2.dp else 7.dp
                    val yIcon = if (data.type == HealthMetricType.MOVE) 0.dp else 5.dp
                    Icon(
                        painter = painterResource(id = data.type.iconResId),
                        contentDescription = null,
                        modifier = Modifier
                            .offset(x = 2.dp, y = yShadow)
                            .blur(radius = 3.dp)
                            .size(smallRingSize * 0.4f),
                        tint = Color.Black.copy(alpha = 0.3f)
                    )
                    Icon(
                        painter = painterResource(id = data.type.iconResId),
                        contentDescription = data.name,
                        tint = data.primaryColor,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(x = 0.dp, y = yIcon)
                            .size(smallRingSize * 0.4f)
                    )
                }
            }
        }
    }
}


@Composable
fun HealthRing(
    progress: Float,
    color: Color,
    size: Dp,
    strokeWidth: Dp,
    isSelected: Boolean = false
) {
    Canvas(modifier = Modifier.size(size)) {
        val ringRadius = (this.size.minDimension / 2) - strokeWidth.toPx() / 2
        drawCircle(
            color = if (isSelected) color.copy(alpha = 0.4f) else Color.LightGray.copy(alpha = 0.3f),
            radius = ringRadius,
            style = Stroke(width = strokeWidth.toPx())
        )
        if (progress > 0f) {
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360 * progress,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
                topLeft = Offset(
                    (this.size.width / 2) - ringRadius,
                    (this.size.height / 2) - ringRadius
                ),
                size = Size(ringRadius * 2, ringRadius * 2)
            )
        }
    }
}

@Composable
fun HealthDetailText(data: HealthData) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = data.name,
            style = MaterialTheme.typography.bodyLarge
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                val yShadow = if (data.type == HealthMetricType.MOVE) 2.dp else 7.dp
                val yIcon = if (data.type == HealthMetricType.MOVE) 0.dp else 5.dp

                Icon(
                    painter = painterResource(id = data.type.iconResId),
                    contentDescription = null,
                    modifier = Modifier
                        .offset(x = 2.dp, y = yShadow)
                        .blur(radius = 4.dp)
                        .size(48.dp),
                    tint = Color.Black.copy(alpha = 0.3f)
                )
                Icon(
                    painter = painterResource(id = data.type.iconResId),
                    contentDescription = data.name,
                    tint = data.primaryColor,
                    modifier = Modifier
                        .size(48.dp)
                        .offset(x = 0.dp, y = yIcon)
                )
            }
            Text(
                text = "${data.currentValue.toInt()}/${data.goalValue.toInt()}",
                color = data.primaryColor,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
            Text(
                text = data.unit,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
            )
        }
    }
}

private fun convertCalenderStateToHealthDataMap(
    calenderState: CalenderState,
    referenceDateForWeek: LocalDate
): Map<LocalDate, DailyHealthReport> {
    val reports = mutableMapOf<LocalDate, DailyHealthReport>()
    val weekStartDate = referenceDateForWeek.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))

    for (i in 0..6) {
        val date = weekStartDate.plusDays(i.toLong())
        val moveGoal = 130f
        val heartRateGoal = 120f
        val sleepGoal = 8f
        val stepsGoal = 10000f
        val walkTimeGoal = 3f

        val currentSteps = calenderState.weeklySteps.getOrElse(i) { 0L }.toFloat()
        val currentCalories = calenderState.weeklyCalories.getOrElse(i) { 0L }.toFloat()
        val currentDistance = calenderState.weeklyDistance.getOrElse(i) { 0L }.toFloat() / 1000f
        val currentHeartRate = calenderState.weeklyHeartRate.getOrElse(i) { 0L }.toFloat()
        val currentSleep = calenderState.weeklySleep.getOrElse(i) { 0L }.toFloat()

        reports[date] = DailyHealthReport(
            move = HealthData(HealthMetricType.MOVE, "ムーブ", Color(0xFFE60039), "KCAL", currentCalories, moveGoal),
            heartRate = HealthData(HealthMetricType.HEART_RATE, "心拍数", Color(0xFFE600A9), "BPM", currentHeartRate, heartRateGoal),
            sleep = HealthData(HealthMetricType.SLEEP, "睡眠時間", Color(0xFF00A9E6), "H", currentSleep, sleepGoal),
            steps = HealthData(HealthMetricType.STEPS, "歩数", Color(0xFF00E6A9), "歩", currentSteps, stepsGoal),
            walkDistance = HealthData(HealthMetricType.WALK_DISTANCE, "歩行時間", Color(0xFFE6A900), "KM", currentDistance, walkTimeGoal)
        )
    }
    return reports
}

private fun createEmptyDailyReport(date: LocalDate): DailyHealthReport {
    val moveGoal = 130f
    val heartRateGoal = 120f
    val sleepGoal = 8f
    val stepsGoal = 10000f
    val walkTimeGoal = 3f
    return DailyHealthReport(
        move = HealthData(HealthMetricType.MOVE, "ムーブ", Color(0xFFE60039), "KCAL", 0f, moveGoal),
        heartRate = HealthData(HealthMetricType.HEART_RATE, "心拍数", Color(0xFFE600A9), "BPM", 0f, heartRateGoal),
        sleep = HealthData(HealthMetricType.SLEEP, "睡眠時間", Color(0xFF00A9E6), "H", 0f, sleepGoal),
        steps = HealthData(HealthMetricType.STEPS, "歩数", Color(0xFF00E6A9), "歩", 0f, stepsGoal),
        walkDistance = HealthData(HealthMetricType.WALK_DISTANCE, "歩行時間", Color(0xFFE6A900), "KM", 0f, walkTimeGoal)
    )
}

@Preview(showBackground = true)
@Composable
fun HealthDashboardScreenPreview() {
    val today = LocalDate.now()
    val sampleReports = (0..6).associate { i ->
        val date = today.minusDays(i.toLong())
        date to createEmptyDailyReport(date)
    }
    MaterialTheme {
        HealthDashboardScreen(
            isLoading = false,
            currentDate = today,
            healthReportsForWeek = sampleReports,
            onDateSelected = {}
        )
    }
}
