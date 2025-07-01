package com.example.myaku_rismu.feature.calender

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

// --- 데이터 모델링 (변경 없음) ---

enum class HealthMetricType { MOVE, HEART_RATE, SLEEP, STEPS, WALK_TIME }

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

// --- 샘플 데이터 (기간 수정) ---

fun generateSampleData(): Map<LocalDate, DailyHealthReport> {
    val today = LocalDate.of(2025, 7, 1) // 오늘 날짜를 7월 1일로 고정
    val data = mutableMapOf<LocalDate, DailyHealthReport>()

    // 6월 25일부터 7월 1일까지 (총 7일) 데이터 생성
    for (i in 0..6) {
        val date = today.minusDays(i.toLong())
        val randomFactor = (0.6f + (i * 0.05f)).coerceAtMost(1.0f) // 오래될수록 값이 낮아짐

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


// --- 화면 구현 ---

@Composable
fun HealthDashboardScreen() {
    val healthDataByDate by remember { mutableStateOf(generateSampleData()) }
    var selectedDate by remember { mutableStateOf(LocalDate.of(2025, 7, 1)) }
    var selectedMetricType by remember { mutableStateOf(HealthMetricType.MOVE) }

    val dailyReport = healthDataByDate[selectedDate] ?: healthDataByDate.values.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding(), // 하단 내비게이션 바 영역만 피하도록 수정
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Calender",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Start)
        )

        WeeklyCalendar(
            selectedDate = selectedDate,
            onDateSelected = { date ->
                if (healthDataByDate.containsKey(date)) {
                    selectedDate = date
                }
            },
            healthReports = healthDataByDate
        )

        Spacer(modifier = Modifier.height(36.dp)) // <-- 이제 이 값을 조절하면 됩니다.

        val dateFormatter = DateTimeFormatter.ofPattern("M月d日", Locale.JAPAN)
        Text(
            text = selectedDate.format(dateFormatter),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // weight(1f)를 제거하고 크기를 직접 지정합니다.
        CircularHealthDashboard(
            report = dailyReport,
            selectedMetricType = selectedMetricType,
            onMetricSelected = { type -> selectedMetricType = type },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp) // <-- 원의 위아래 여백
                .aspectRatio(1f) // 1:1 비율 (정사각형)
        )

        val selectedData = dailyReport.getByType(selectedMetricType)
        HealthDetail(data = selectedData)

        // 화면 하단과 HealthDetail 사이에 공간을 주기 위해 Spacer 추가
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun WeeklyCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    healthReports: Map<LocalDate, DailyHealthReport>
) {
    var displayDate by remember(selectedDate) { mutableStateOf(selectedDate) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = { displayDate = displayDate.minusDays(1) }) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "이전")
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val weekStart = displayDate.minusDays(3)
            for (i in 0..6) {
                val date = weekStart.plusDays(i.toLong())
                val hasData = healthReports.containsKey(date)
                val progress = if(hasData) healthReports[date]!!.move.progress else 0f
                val color = if(hasData) healthReports[date]!!.move.primaryColor else Color.LightGray

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
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "다음")
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
        animationSpec = tween(durationMillis = 1000), label = "main progress animation"
    )

    BoxWithConstraints(
        modifier = modifier.padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        val boxSize = minOf(maxWidth, maxHeight)

        // --- 큰 링 ---
        HealthRing(
            progress = animatedProgress,
            color = mainData.primaryColor,
            size = boxSize,
            strokeWidth = 20.dp
        )
        Box(
            modifier = Modifier
                .size(boxSize - 40.dp)
                .clip(CircleShape)
                .background(mainData.primaryColor.copy(alpha = 0.1f))
        )

        // --- 작은 링 ---
        val radius = boxSize * 0.25f
        val smallRingSize = boxSize * 0.28f

        // ★★★ 1. 각 슬롯의 고정된 위치(각도)와 기본 타입을 정의합니다. ★★★
        // 순서: 위(心拍数), 오른쪽(歩数), 아래(歩行時間), 왼쪽(睡眠時間)
        val slots = listOf(
            Pair(-Math.PI / 2, HealthMetricType.HEART_RATE),
            Pair(0.0, HealthMetricType.STEPS),
            Pair(Math.PI / 2, HealthMetricType.WALK_TIME),
            Pair(Math.PI, HealthMetricType.SLEEP)
        )

        // ★★★ 2. 고정된 슬롯을 기준으로 작은 링들을 그립니다. ★★★
        slots.forEach { (angle, defaultTypeInSlot) ->

            // 이 슬롯에 실제로 표시될 타입을 결정합니다.
            val typeToShowInSlot = when {
                // 만약 현재 선택된 타입이 'ムーブ'가 아니고, 이 슬롯의 기본 타입과 같다면,
                // 이 슬롯에는 'ムーブ'를 표시합니다.
                selectedMetricType != HealthMetricType.MOVE && defaultTypeInSlot == selectedMetricType -> {
                    HealthMetricType.MOVE
                }
                // 그 외의 모든 경우에는 슬롯의 기본 타입을 표시합니다.
                else -> {
                    defaultTypeInSlot
                }
            }

            // 표시될 데이터
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
                    .clickable { onMetricSelected(typeToShowInSlot) } // 표시된 타입을 클릭 이벤트에 전달
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
fun HealthDetail(data: HealthData) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = data.icon,
            contentDescription = data.name,
            tint = data.primaryColor,
            modifier = Modifier.size(24.dp)
        )
        Text(text = data.name, fontSize = 16.sp)
        Text(
            text = "${data.currentValue.toInt()}/${data.goalValue.toInt()}",
            color = data.primaryColor,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(text = data.unit, fontSize = 16.sp)
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
fun HealthDashboardScreenPreview() {
    MaterialTheme {
        HealthDashboardScreen()
    }
}