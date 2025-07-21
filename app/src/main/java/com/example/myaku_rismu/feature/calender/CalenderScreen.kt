package com.example.myaku_rismu.feature.calender

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.AppState


enum class HealthMetricType { MOVE,
    HEART_RATE,
    SLEEP,
    STEPS,
    WALK_TIME }

data class HealthData(
    val type: HealthMetricType,
    val name: String,
    val icon: ImageVector,
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
    val walkTime: HealthData
) {
    fun getByType(type: HealthMetricType): HealthData = when (type) {
        HealthMetricType.MOVE -> move
        HealthMetricType.HEART_RATE -> heartRate
        HealthMetricType.SLEEP -> sleep
        HealthMetricType.STEPS -> steps
        HealthMetricType.WALK_TIME -> walkTime
    }
}

//sample just 7days
fun generateSampleData(): Map<LocalDate, DailyHealthReport> {
    val today = LocalDate.now()
    val data = mutableMapOf<LocalDate, DailyHealthReport>()

    for (i in 0..6) {
        val date = today.minusDays(i.toLong())
        val randomFactor = (0.6f + (i * 0.05f)).coerceAtMost(1.0f)

        val isToday = i == 0
        data[date] = DailyHealthReport(
            move = HealthData(HealthMetricType.MOVE, "ムーブ", Icons.Default.DirectionsRun, Color(0xFFE60039), "KCAL", if (isToday) 50f else 130f * randomFactor, 130f),
            heartRate = HealthData(HealthMetricType.HEART_RATE, "心拍数", Icons.Default.Favorite, Color(0xFFE600A9), "BPM", if (isToday) 80f else (70..90).random().toFloat(), 120f),
            sleep = HealthData(HealthMetricType.SLEEP, "睡眠時間", Icons.Default.Hotel, Color(0xFF00A9E6), "H", if (isToday) 7.5f else (6..8).random().toFloat(), 8f),
            steps = HealthData(HealthMetricType.STEPS, "歩数", Icons.Default.DirectionsWalk, Color(0xFF00E6A9), "歩", if (isToday) 6000f else (4000..11000).random().toFloat(), 10000f),
            walkTime = HealthData(HealthMetricType.WALK_TIME, "歩行時間", Icons.Default.Event, Color(0xFFE6A900), "KM", if (isToday) 1.4f else (1..3).random().toFloat(), 3f)
        )
    }
    return data
}

@Composable
fun CalenderScreen(appState: AppState, modifier: Modifier = Modifier) {
}

@Composable
fun HealthDashboardScreen() {
    val healthDataByDate by remember { mutableStateOf(generateSampleData()) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedMetricType by remember { mutableStateOf(HealthMetricType.MOVE) }

    val dailyReport = healthDataByDate[selectedDate] ?: healthDataByDate.values.first()

    val dateFormatPattern = stringResource(id = R.string.calender_screen_date)
    val dateFormatter = remember(dateFormatPattern) {
        DateTimeFormatter.ofPattern(dateFormatPattern, Locale.JAPAN)
    }
    val selectedData = dailyReport.getByType(selectedMetricType)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        WeeklyCalendar(
            selectedDate = selectedDate,
            onDateSelected = { date ->
                if (healthDataByDate.containsKey(date)) {
                    selectedDate = date
                }
            },
            healthReports = healthDataByDate
        )

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = selectedDate.format(dateFormatter),
            style = MaterialTheme.typography.headlineMedium,
        )

        CircularHealthDashboard(
            report = dailyReport,
            selectedMetricType = selectedMetricType,
            onMetricSelected = { type -> selectedMetricType = type },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(vertical = 16.dp)
        )

        HealthDetailText(data = selectedData)
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

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        HealthRing(
            progress = animatedProgress,
            color = mainData.primaryColor,
            size = LocalConfiguration.current.screenWidthDp.dp * 0.9f,
            strokeWidth = 20.dp
        )

        val radius = 80.dp
        val smallRingSize = 80.dp

        val slots = listOf(
            Pair(-Math.PI / 2, HealthMetricType.HEART_RATE),
            Pair(0.0, HealthMetricType.STEPS),
            Pair(Math.PI / 2, HealthMetricType.WALK_TIME),
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
                    strokeWidth = 6.dp
                )
                Icon(
                    imageVector = data.icon,
                    contentDescription = data.name,
                    tint = data.primaryColor,
                    modifier = Modifier.align(Alignment.Center)
                )
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
            color = if (isSelected) color.copy(alpha = 0.5f) else Color.LightGray.copy(alpha = 0.3f),
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

@Preview(showBackground = true)
@Composable
fun HealthDashboardScreenPreview() {
    MaterialTheme {
        HealthDashboardScreen()
    }
}