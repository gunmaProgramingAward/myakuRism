package com.example.myaku_rismu.feature.calender

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.DirectionsWalk
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
import com.example.myaku_rismu.data.model.RecordType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.ui.theme.RingInactiveColor
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.temporal.TemporalAdjusters
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun CalenderScreen(
    appState: AppState,
    modifier: Modifier = Modifier,
    viewModel: CalenderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    fun eventHandler(event: CalenderUiEvent) {
        when (event) {
            is CalenderUiEvent.OnDateSelected -> {
                viewModel.selectDate(event.date)
            }
            is CalenderUiEvent.LoadHealthData -> {
                viewModel.getHealthData(event.date)
            }
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.getHealthData(LocalDate.now())
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var currentDisplayDate by remember(uiState.selectedDate) { mutableStateOf(uiState.selectedDate) }

    LaunchedEffect(uiState.screenState) {
        if (uiState.screenState is ScreenState.Error) {
            val errorState = uiState.screenState as ScreenState.Error
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = "エラー: ${errorState.message}",
                    duration = SnackbarDuration.Short
                )
            }
            viewModel.clearError()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->
        when (uiState.screenState) {
            is ScreenState.Initializing -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(contentPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is ScreenState.Success, is ScreenState.Error -> {
                val healthDataByDateFromState: Map<LocalDate, DailyHealthReport> =
                    convertCalenderStateToHealthDataMap(uiState, currentDisplayDate)

                HealthDashboardScreen(
                    modifier = Modifier.padding(contentPadding),
                    currentDate = currentDisplayDate,
                    healthReportsForWeek = healthDataByDateFromState,
                    onDateSelected = { selectedDate ->
                        currentDisplayDate = selectedDate
                        eventHandler(CalenderUiEvent.OnDateSelected(selectedDate))
                    },
                    onWeekChanged = { newDate ->
                        eventHandler(CalenderUiEvent.LoadHealthData(newDate))
                    }
                )
            }
        }
    }
}


@Composable
fun HealthDashboardScreen(
    modifier: Modifier = Modifier,
    currentDate: LocalDate,
    healthReportsForWeek: Map<LocalDate, DailyHealthReport>,
    onDateSelected: (LocalDate) -> Unit,
    onWeekChanged: (LocalDate) -> Unit
) {
    var selectedMetricTypeState by remember { mutableStateOf(RecordType.CALORIES) }
    val dailyReport = healthReportsForWeek[currentDate]
        ?: createEmptyDailyReport()

    val dateFormatPattern = stringResource(id = R.string.calender_screen_date)
    val dateFormatter = remember(dateFormatPattern) {
        DateTimeFormatter.ofPattern(dateFormatPattern, Locale.JAPAN)
    }
    val selectedData = dailyReport.getByType(selectedMetricTypeState)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        WeeklyCalendar(
            selectedDate = currentDate,
            onDateSelected = onDateSelected,
            healthReports = healthReportsForWeek,
            onWeekChanged = onWeekChanged
        )

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = currentDate.format(dateFormatter),
            style = MaterialTheme.typography.headlineMedium,
        )

        CircularHealthDashboard(
            report = dailyReport,
            selectedMetricType = selectedMetricTypeState,
            onMetricSelected = { type -> selectedMetricTypeState = type },
            modifier = Modifier.fillMaxWidth().weight(1f).padding(vertical = 16.dp)
        )
        HealthDetailText(data = selectedData)
    }
}

@Composable
fun WeeklyCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    healthReports: Map<LocalDate, DailyHealthReport>,
    onWeekChanged: (LocalDate) -> Unit,
) {
    var displayDate by remember(selectedDate) { mutableStateOf(selectedDate) }
    val weekStart = displayDate.minusDays(6)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
                val newDate = displayDate.minusWeeks(1)
                displayDate = newDate
                onWeekChanged(newDate)
            }) {
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

                    val progressColor = report?.calories?.primaryColor ?: Color.LightGray
                    val backgroundColor = if (date == selectedDate) {
                        progressColor.copy(alpha = 0.4f)
                    } else {
                        RingInactiveColor.copy(alpha = 0.3f)
                    }

                    HealthRing(
                        progress = report?.calories?.progress ?: 0f,
                        progressColor = progressColor,
                        backgroundColor = backgroundColor,
                        size = 32.dp,
                        strokeWidth = 3.dp
                    )
                }
            }
        }

        IconButton(onClick = {
            val newDate = displayDate.plusWeeks(1)
            displayDate = newDate
            onWeekChanged(newDate)
        }) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(id = R.string.calender_screen_next))
        }
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CircularHealthDashboard(
    report: DailyHealthReport,
    selectedMetricType: RecordType,
    onMetricSelected: (RecordType) -> Unit,
    modifier: Modifier = Modifier
) {
    val mainData = report.getByType(selectedMetricType)

    val animatedProgress by animateFloatAsState(
        targetValue = mainData.progress,
        animationSpec = tween(durationMillis = 1000),
        label = stringResource(id = R.string.calender_screen_animation)
    )

    val inactiveRingColor = Color.LightGray.copy(alpha = 0.3f)

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val dashboardSize = minOf(maxWidth, maxHeight) * 0.95f

        HealthRing(
            progress = animatedProgress,
            progressColor = mainData.primaryColor,
            backgroundColor = inactiveRingColor,
            size = dashboardSize,
            strokeWidth = dashboardSize * 0.07f
        )

        val radius = dashboardSize * 0.23f
        val smallRingSize = dashboardSize * 0.25f

        val slots = listOf(
            Pair(-Math.PI / 2, RecordType.HEART_RATE),
            Pair(0.0, RecordType.STEPS),
            Pair(Math.PI / 2, RecordType.DISTANCE),
            Pair(Math.PI, RecordType.SLEEP_TIME)
        )

        slots.forEach { (angle, defaultTypeInSlot) ->
            val typeToShowInSlot = when {
                selectedMetricType != RecordType.CALORIES && defaultTypeInSlot == selectedMetricType -> {
                    RecordType.CALORIES
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
                    progressColor = data.primaryColor,
                    backgroundColor = inactiveRingColor,
                    size = smallRingSize,
                    strokeWidth = smallRingSize * 0.1f,
                )
                Icon(
                    imageVector = data.icon,
                    contentDescription = data.name,
                    tint = data.primaryColor,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(smallRingSize * 0.4f)
                )
            }
        }
    }
}


@Composable
fun HealthRing(
    progress: Float,
    progressColor: Color,
    backgroundColor: Color,
    size: Dp,
    strokeWidth: Dp,
) {
    Canvas(modifier = Modifier.size(size)) {
        val ringRadius = (this.size.minDimension / 2) - strokeWidth.toPx() / 2
        drawCircle(
            color = backgroundColor,
            radius = ringRadius,
            style = Stroke(width = strokeWidth.toPx())
        )
        if (progress > 0f) {
            drawArc(
                color = progressColor,
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
            style = MaterialTheme.typography.headlineSmall
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = data.icon,
                contentDescription = data.name,
                tint = data.primaryColor,
                modifier = Modifier.size(32.dp)
            )
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
    val weekStartDate = referenceDateForWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    for (i in 0..6) {
        // 仮の値
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
            calories = HealthData(RecordType.CALORIES, "ムーブ", Icons.Default.DirectionsRun, Color(0xFFE60039), "KCAL", currentCalories, moveGoal),
            heartRate = HealthData(RecordType.HEART_RATE, "心拍数", Icons.Default.Favorite, Color(0xFFE600A9), "BPM", currentHeartRate, heartRateGoal),
            sleepTime = HealthData(RecordType.SLEEP_TIME, "睡眠時間", Icons.Default.Hotel, Color(0xFF00A9E6), "H", currentSleep, sleepGoal),
            steps = HealthData(RecordType.STEPS, "歩数", Icons.Default.DirectionsWalk, Color(0xFF00E6A9), "歩", currentSteps, stepsGoal),
            distance = HealthData(RecordType.DISTANCE, "歩行時間", Icons.Default.Event, Color(0xFFE6A900), "KM", currentDistance, walkTimeGoal)
        )
    }
    return reports
}

private fun createEmptyDailyReport(): DailyHealthReport {
    // 仮の値
    val moveGoal = 130f
    val heartRateGoal = 120f
    val sleepGoal = 8f
    val stepsGoal = 10000f
    val walkTimeGoal = 3f
    return DailyHealthReport(
        calories = HealthData(RecordType.CALORIES, "ムーブ", Icons.Default.DirectionsRun, Color(0xFFE60039), "KCAL", 0f, moveGoal),
        heartRate = HealthData(RecordType.HEART_RATE, "心拍数", Icons.Default.Favorite, Color(0xFFE600A9), "BPM", 0f, heartRateGoal),
        sleepTime = HealthData(RecordType.SLEEP_TIME, "睡眠時間", Icons.Default.Hotel, Color(0xFF00A9E6), "H", 0f, sleepGoal),
        steps = HealthData(RecordType.STEPS, "歩数", Icons.Default.DirectionsWalk, Color(0xFF00E6A9), "歩", 0f, stepsGoal),
        distance = HealthData(RecordType.DISTANCE, "歩行時間", Icons.Default.Event, Color(0xFFE6A900), "KM", 0f, walkTimeGoal)
    )
}

@Preview(showBackground = true)
@Composable
fun HealthDashboardScreenPreview() {
    val today = LocalDate.now()
    val sampleReports = (0..6).associate { i ->
        val date = today.minusDays(i.toLong())
        date to createEmptyDailyReport()
    }
    MaterialTheme {
        HealthDashboardScreen(
            currentDate = today,
            healthReportsForWeek = sampleReports,
            onDateSelected = {},
            onWeekChanged = {}
        )
    }
}