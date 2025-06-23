package com.example.myaku_rismu.feature.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myaku_rismu.R
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme
import java.util.Calendar
import kotlin.math.abs
import kotlin.math.pow
import kotlin.text.toIntOrNull
import kotlin.math.max as mathMax

// --- 定数 ---
val genderDisplayOptions = listOf("男性", "女性", "その他", "回答しない")
const val UNSET_INT_VALUE = -1 // 未設定を示す整数値
const val COMMON_PLACEHOLDER = "未設定"

// --- デフォルトのピッカースタイルパラメータ ---
private val defaultPickerItemHeight = 48.dp
private const val defaultPickerVisibleItemsCount = 5
private val defaultPickerTextStyleNormal = 20.sp
private val defaultPickerTextStyleSelected = 28.sp


@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    val activityLevels =
        listOf("低い\n座りがちな生活", "普通\n週2-3回の軽い運動", "高い\n週4回以上の運動")
    var selectedActivity by remember { mutableIntStateOf(1) }

    val calendar = Calendar.getInstance()
    var selectedYear by remember { mutableStateOf<Int?>(null) }
    var selectedMonth by remember { mutableStateOf<Int?>(null) }
    var selectedDay by remember { mutableStateOf<Int?>(null) }

    var currentHeightCm by remember { mutableStateOf<Int?>(null) }
    var currentWeightKg by remember { mutableStateOf<Int?>(null) }
    var selectedGenderIndex by remember { mutableIntStateOf(UNSET_INT_VALUE) }

    var showBirthdateDialog by remember { mutableStateOf(false) }
    var showHeightDialog by remember { mutableStateOf(false) }
    var showWeightDialog by remember { mutableStateOf(false) }
    var showGenderDialog by remember { mutableStateOf(false) }

    // --- ダイアログ表示制御 ---
    if (showBirthdateDialog) {
        ModernBirthdatePickerDialog(
            initialYear = selectedYear ?: (calendar.get(Calendar.YEAR) - 25),
            initialMonth = selectedMonth ?: (calendar.get(Calendar.MONTH) + 1),
            initialDay = selectedDay ?: calendar.get(Calendar.DAY_OF_MONTH),
            onBirthdateSelected = { year, month, day ->
                selectedYear = year
                selectedMonth = month
                selectedDay = day
                showBirthdateDialog = false
            },
            onDismiss = { showBirthdateDialog = false }
        )
    }

    if (showHeightDialog) {
        ModernStringOrNumberPickerDialog(
            title = stringResource(R.string.select_height),
            options = (100..220).map { it.toString() },
            currentValue = currentHeightCm?.toString(),
            onValueSelected = { selectedString ->
                currentHeightCm = selectedString.toIntOrNull()
                showHeightDialog = false
            },
            onDismiss = { showHeightDialog = false },
            unitSuffix = "cm",
            unitSuffixTextStyle = defaultPickerTextStyleSelected.times(0.85f)
        )
    }

    if (showWeightDialog) {
        ModernStringOrNumberPickerDialog(
            title = stringResource(R.string.select_weight),
            options = remember { (30..150).map { it.toString() } },
            currentValue = currentWeightKg?.toString(),
            onValueSelected = { selectedString ->
                currentWeightKg = selectedString.toIntOrNull()
                showWeightDialog = false
            },
            onDismiss = { showWeightDialog = false },
            unitSuffix = "kg",
            unitSuffixTextStyle = defaultPickerTextStyleSelected.times(0.85f)
        )
    }

    if (showGenderDialog) {
        ModernStringOrNumberPickerDialog(
            title = stringResource(R.string.select_gender),
            options = genderDisplayOptions,
            currentValue = if (selectedGenderIndex != UNSET_INT_VALUE && selectedGenderIndex in genderDisplayOptions.indices) {
                genderDisplayOptions[selectedGenderIndex]
            } else null,
            onValueSelected = { selectedString ->
                selectedGenderIndex = genderDisplayOptions.indexOf(selectedString)
                showGenderDialog = false
            },
            onDismiss = { showGenderDialog = false }
        )
    }
    // --- ダイアログ表示制御ここまで ---


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            stringResource(R.string.profile),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(31.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        stringResource(R.string.basic_information),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                val infoItems = listOf<@Composable () -> Unit>(
                    {
                        InfoItem(
                            label = "生年月日",
                            value = if (selectedYear != null && selectedMonth != null && selectedDay != null) {
                                "${selectedYear}年 ${selectedMonth}月 ${selectedDay}日"
                            } else COMMON_PLACEHOLDER,
                            onClick = { showBirthdateDialog = true }
                        )
                    },
                    {
                        InfoItem(
                            label = "身長",
                            value = currentHeightCm?.let { "$it cm" } ?: COMMON_PLACEHOLDER,
                            onClick = { showHeightDialog = true }
                        )
                    },
                    {
                        InfoItem(
                            label = "体重",
                            // ★ currentWeightKg は Int なので、そのまま "kg" を付けて表示
                            value = currentWeightKg?.let { "$it kg" } ?: COMMON_PLACEHOLDER,
                            onClick = { showWeightDialog = true }
                        )
                    },
                    {
                        InfoItem(
                            label = "性別",
                            value = if (selectedGenderIndex != UNSET_INT_VALUE && selectedGenderIndex in genderDisplayOptions.indices) {
                                genderDisplayOptions[selectedGenderIndex]
                            } else COMMON_PLACEHOLDER,
                            onClick = { showGenderDialog = true }
                        )
                    }
                )

                // InfoItems の表示ロジック
                (0 until infoItems.size step 2).forEach { rowIndex ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) { infoItems[rowIndex]() }
                        if (rowIndex + 1 < infoItems.size) {
                            Box(modifier = Modifier.weight(1f)) { infoItems[rowIndex + 1]() }
                        } else {
                            Spacer(modifier = Modifier.weight(1f)) // 奇数個の場合のスペーサー
                        }
                    }
                    if (rowIndex + 1 < infoItems.size - 1) { // 最後の行でなければスペース追加
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }

        // 活動レベルカード
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.event),
                        contentDescription = "活動レベルアイコン",
                        modifier = Modifier.size(31.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        stringResource(R.string.activity_level),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                activityLevels.forEachIndexed { index, text ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = if (selectedActivity == index) MaterialTheme.colorScheme.primaryContainer.copy(
                                    alpha = 0.2f
                                ) else Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(
                                width = 1.5.dp,
                                color = if (selectedActivity == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .selectable(
                                selected = selectedActivity == index,
                                onClick = { selectedActivity = index },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        RadioButton(
                            selected = selectedActivity == index,
                            onClick = { selectedActivity = index },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                        )
                    }
                    if (index < activityLevels.size - 1) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

// --- プロフィール情報表示用の小コンポーネント ---
@Composable
private fun InfoItem(label: String, value: String, onClick: () -> Unit) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 2.dp)
        )
        SelectableInfoField(
            text = value,
            onClick = onClick
        )
    }
}

// --- クリック可能な情報表示フィールド ---
@Composable
private fun SelectableInfoField(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val fieldShape = OutlinedTextFieldDefaults.shape
    val currentTextColor = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    val currentBorderColor = if (enabled) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(
                minWidth = OutlinedTextFieldDefaults.MinWidth,
                minHeight = OutlinedTextFieldDefaults.MinHeight
            )
            .border(
                width = OutlinedTextFieldDefaults.UnfocusedBorderThickness,
                brush = SolidColor(currentBorderColor),
                shape = fieldShape
            )
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = ripple()
            )
            .padding(OutlinedTextFieldDefaults.contentPadding()),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            // ★ プレースホルダーのテキスト色を共通プレースホルダーで判定
            color = if (text == COMMON_PLACEHOLDER && enabled) {
                currentTextColor.copy(alpha = 0.6f)
            } else {
                currentTextColor
            },
            textAlign = TextAlign.Start
        )
    }
}

// --- 文字列または数値選択用のモダンなピッカーダイアログ ---
@Composable
fun ModernStringOrNumberPickerDialog(
    title: String,
    options: List<String>,
    currentValue: String?,
    onValueSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    unitSuffix: String = "",
    itemHeight: Dp = defaultPickerItemHeight,
    visibleItemsCount: Int = defaultPickerVisibleItemsCount,
    textStyleNormal: TextUnit = defaultPickerTextStyleNormal,
    textStyleSelected: TextUnit = defaultPickerTextStyleSelected,
    unitSuffixTextStyle: TextUnit = textStyleSelected
) {
    if (options.isEmpty()) {
        LaunchedEffect(Unit) { onDismiss() }
        return
    }

    val halfVisibleItems = remember(visibleItemsCount) { (visibleItemsCount - 1) / 2 }
    val listState = rememberLazyListState()
    val density = LocalDensity.current

    val actualInitialIndex = remember(options, currentValue) {
        val idx = options.indexOf(currentValue)
        if (idx != -1) idx else (options.size / 2).coerceAtLeast(0)
    }.coerceIn(0, mathMax(0, options.size - 1))

    val scrollToInitialIndex = actualInitialIndex + halfVisibleItems
    val totalLayoutHeight = itemHeight * visibleItemsCount

    val centralVisibleOptionIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty() || options.isEmpty()) {
                actualInitialIndex
            } else {
                val viewportCenterY = layoutInfo.viewportStartOffset + layoutInfo.viewportSize.height / 2
                layoutInfo.visibleItemsInfo
                    .filter { it.index >= halfVisibleItems && it.index < options.size + halfVisibleItems }
                    .minByOrNull { abs((it.offset + it.size / 2) - viewportCenterY) }
                    ?.let { it.index - halfVisibleItems }
                    ?.coerceIn(0, mathMax(0, options.size - 1))
                    ?: actualInitialIndex
            }
        }
    }

    LaunchedEffect(listState, scrollToInitialIndex, options.size, itemHeight, totalLayoutHeight, density) {
        if (options.isNotEmpty() && scrollToInitialIndex < listState.layoutInfo.totalItemsCount) {
            val targetOffset = (with(density) { totalLayoutHeight.toPx() / 2 - itemHeight.toPx() / 2 }).toInt()
            listState.scrollToItem(scrollToInitialIndex, scrollOffset = -targetOffset)
        }
    }


    val snappingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val pickerWidth = maxWidth * 0.8f

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .height(totalLayoutHeight)
                            .width(pickerWidth),
                        contentAlignment = Alignment.Center
                    ) {
                        LazyColumn(
                            state = listState,
                            flingBehavior = snappingBehavior,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(halfVisibleItems) {
                                Spacer(modifier = Modifier.height(itemHeight))
                            }

                            items(options.size, key = { index -> options[index] + "_picker_item" }) { optionIndex ->
                                val optionValue = options[optionIndex]
                                val isSelected = optionIndex == centralVisibleOptionIndex

                                val distanceToCenterNormalizedAbs = abs(optionIndex - centralVisibleOptionIndex)
                                val scaleFactor = ((halfVisibleItems - distanceToCenterNormalizedAbs).toFloat() / halfVisibleItems * 0.25f + 0.75f)
                                    .coerceIn(0.75f, 1f)
                                val alphaFactor = ((halfVisibleItems - distanceToCenterNormalizedAbs).toFloat() / halfVisibleItems * 0.6f + 0.4f)
                                    .coerceIn(0.25f, 1f)

                                Box(
                                    modifier = Modifier
                                        .height(itemHeight)
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp)
                                        .graphicsLayer {
                                            if (visibleItemsCount > 1) {
                                                scaleX = if (isSelected) 1f else scaleFactor
                                                scaleY = if (isSelected) 1f else scaleFactor
                                                alpha =
                                                    if (isSelected) 1f else alphaFactor.pow(1.2f)
                                            }
                                            transformOrigin = TransformOrigin.Center
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected && unitSuffix.isNotBlank()) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = optionValue,
                                                style = MaterialTheme.typography.displaySmall.copy(
                                                    fontSize = textStyleSelected,
                                                    color = MaterialTheme.colorScheme.primary
                                                ),
                                                textAlign = TextAlign.End
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = unitSuffix,
                                                style = MaterialTheme.typography.displaySmall.copy(
                                                    fontSize = unitSuffixTextStyle,
                                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                                                ),
                                                textAlign = TextAlign.Start
                                            )
                                        }
                                    } else {
                                        Text(
                                            text = optionValue,
                                            style = MaterialTheme.typography.displaySmall.copy(
                                                fontSize = if (isSelected) textStyleSelected else textStyleNormal,
                                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }

                            items(halfVisibleItems) {
                                Spacer(modifier = Modifier.height(itemHeight))
                            }
                        }

                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            val indicatorLineWidth = pickerWidth * 0.85f
                            HorizontalDivider(
                                modifier = Modifier
                                    .width(indicatorLineWidth)
                                    .offset(y = -itemHeight / 2),
                                thickness = 1.5.dp,
                                color = indicatorColor
                            )
                            HorizontalDivider(
                                modifier = Modifier
                                    .width(indicatorLineWidth)
                                    .offset(y = itemHeight / 2),
                                thickness = 1.5.dp,
                                color = indicatorColor
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (centralVisibleOptionIndex < options.size && options.isNotEmpty()) {
                    onValueSelected(options[centralVisibleOptionIndex])
                }
                onDismiss()
            }) { Text("OK", color = MaterialTheme.colorScheme.primary) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("キャンセル", color = MaterialTheme.colorScheme.primary) }
        }
    )
}

// --- 生年月日選択用のモダンなピッカーダイアログ ---
@Composable
fun ModernBirthdatePickerDialog(
    initialYear: Int,
    initialMonth: Int,
    initialDay: Int,
    onBirthdateSelected: (year: Int, month: Int, day: Int) -> Unit,
    onDismiss: () -> Unit
) {
    val currentCalendar = Calendar.getInstance()
    val maxYear = currentCalendar.get(Calendar.YEAR)
    val minYear = maxYear - 100

    var tempYear by remember { mutableIntStateOf(initialYear.coerceIn(minYear, maxYear)) }
    var tempMonth by remember { mutableIntStateOf(initialMonth.coerceIn(1, 12)) }

    val daysInMonth = remember(tempYear, tempMonth) {
        val cal = Calendar.getInstance().apply {
            clear()
            set(Calendar.YEAR, tempYear)
            set(Calendar.MONTH, tempMonth - 1)
        }
        cal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
    var tempDay by remember { mutableIntStateOf(initialDay.coerceIn(1, daysInMonth)) }

    LaunchedEffect(daysInMonth) {
        if (tempDay > daysInMonth) {
            tempDay = daysInMonth
        }
    }

    val yearOptions = remember { (minYear..maxYear).map { it.toString() } }
    val monthOptions = remember { (1..12).map { it.toString() } }
    val dayOptions = remember(daysInMonth) { (1..daysInMonth).map { it.toString() } }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "生年月日を選択",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ModernPickerColumnInternal(
                    options = yearOptions,
                    currentValue = tempYear.toString(),
                    onValueSelected = { tempYear = it.toInt() },
                    modifier = Modifier.weight(1.3f),
                    unitSuffix = "年",
                    unitSuffixTextStyle = MaterialTheme.typography.bodyMedium.fontSize,
                    itemHeight = defaultPickerItemHeight,
                    visibleItemsCount = defaultPickerVisibleItemsCount
                )
                ModernPickerColumnInternal(
                    options = monthOptions,
                    currentValue = tempMonth.toString(),
                    onValueSelected = { tempMonth = it.toInt() },
                    modifier = Modifier.weight(1f),
                    unitSuffix = "月",
                    unitSuffixTextStyle = MaterialTheme.typography.bodyMedium.fontSize,
                    itemHeight = defaultPickerItemHeight,
                    visibleItemsCount = defaultPickerVisibleItemsCount
                )
                ModernPickerColumnInternal(
                    options = dayOptions,
                    currentValue = tempDay.toString(),
                    onValueSelected = { tempDay = it.toInt() },
                    modifier = Modifier.weight(1f),
                    unitSuffix = "日",
                    unitSuffixTextStyle = MaterialTheme.typography.bodyMedium.fontSize,
                    itemHeight = defaultPickerItemHeight,
                    visibleItemsCount = defaultPickerVisibleItemsCount
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onBirthdateSelected(tempYear, tempMonth, tempDay)
                    onDismiss()
                }
            ) { Text("OK", color = MaterialTheme.colorScheme.primary) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("キャンセル", color = MaterialTheme.colorScheme.primary) }
        }
    )
}

// --- 単一カラムのピッカー内部実装 (ModernBirthdatePickerDialog で使用) ---
@Composable
private fun ModernPickerColumnInternal(
    options: List<String>,
    currentValue: String,
    onValueSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    itemHeight: Dp = defaultPickerItemHeight,
    visibleItemsCount: Int = defaultPickerVisibleItemsCount,
    textStyleNormal: TextUnit = defaultPickerTextStyleNormal * 0.8f,
    textStyleSelected: TextUnit = defaultPickerTextStyleSelected * 0.9f,
    unitSuffix: String = "",
    unitSuffixTextStyle: TextUnit = textStyleSelected * 0.8f
) {
    if (options.isEmpty()) return

    val halfVisibleItems = remember(visibleItemsCount) { (visibleItemsCount - 1) / 2 }
    val listState = rememberLazyListState()
    val density = LocalDensity.current

    val actualInitialIndex = remember(options, currentValue) {
        options.indexOf(currentValue).takeIf { it != -1 } ?: (options.size / 2).coerceAtLeast(0)
    }.coerceIn(0, mathMax(0, options.size - 1))

    val scrollToInitialIndex = actualInitialIndex + halfVisibleItems
    val totalLayoutHeight = itemHeight * visibleItemsCount

    val centralVisibleOptionIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty() || options.isEmpty()) {
                actualInitialIndex
            } else {
                val viewportCenterY = layoutInfo.viewportStartOffset + layoutInfo.viewportSize.height / 2
                layoutInfo.visibleItemsInfo
                    .filter { it.index >= halfVisibleItems && it.index < options.size + halfVisibleItems }
                    .minByOrNull { abs((it.offset + it.size / 2) - viewportCenterY) }
                    ?.let { it.index - halfVisibleItems }
                    ?.coerceIn(0, mathMax(0, options.size - 1))
                    ?: actualInitialIndex
            }
        }
    }

    LaunchedEffect(listState, scrollToInitialIndex, options.size, itemHeight, totalLayoutHeight, density) {
        if (options.isNotEmpty() && scrollToInitialIndex < listState.layoutInfo.totalItemsCount) {
            val targetOffset = (with(density) { totalLayoutHeight.toPx() / 2 - itemHeight.toPx() / 2 }).toInt()
            listState.scrollToItem(scrollToInitialIndex, scrollOffset = -targetOffset)
        }
    }

    LaunchedEffect(centralVisibleOptionIndex, listState.isScrollInProgress, options) {
        // スクロール中でなく、オプションがあり、中央のインデックスが有効な場合
        if (!listState.isScrollInProgress && options.isNotEmpty() && centralVisibleOptionIndex in options.indices) {
            val selectedOption = options[centralVisibleOptionIndex]
            if (selectedOption != currentValue) { // 実際に値が変更された場合のみコールバック
                onValueSelected(selectedOption)
            }
        }
    }


    val snappingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .height(totalLayoutHeight)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                state = listState,
                flingBehavior = snappingBehavior,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                items(halfVisibleItems) { Spacer(modifier = Modifier.height(itemHeight)) }

                items(options.size, key = { index -> options[index] + "_col_item" }) { optionIndex ->
                    val optionValue = options[optionIndex]
                    val isSelected = optionIndex == centralVisibleOptionIndex

                    val distanceToCenterNormalizedAbs = abs(optionIndex - centralVisibleOptionIndex)
                    val scaleFactor = ((halfVisibleItems - distanceToCenterNormalizedAbs).toFloat() / halfVisibleItems * 0.2f + 0.8f)
                        .coerceIn(0.8f, 1f)
                    val alphaFactor = ((halfVisibleItems - distanceToCenterNormalizedAbs).toFloat() / halfVisibleItems * 0.5f + 0.5f)
                        .coerceIn(0.4f, 1f)

                    Box(
                        modifier = Modifier
                            .height(itemHeight)
                            .fillMaxWidth()
                            .graphicsLayer {
                                if (visibleItemsCount > 1) {
                                    scaleX = if (isSelected) 1f else scaleFactor
                                    scaleY = if (isSelected) 1f else scaleFactor
                                    alpha = if (isSelected) 1f else alphaFactor
                                }
                                transformOrigin = TransformOrigin.Center
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = optionValue,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = if (isSelected) textStyleSelected else textStyleNormal,
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                items(halfVisibleItems) { Spacer(modifier = Modifier.height(itemHeight)) }
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                val indicatorLineModifier = Modifier.fillMaxWidth(0.7f)

                HorizontalDivider(
                    modifier = indicatorLineModifier.offset(y = -itemHeight / 2),
                    thickness = 1.dp,
                    color = indicatorColor
                )
                HorizontalDivider(
                    modifier = indicatorLineModifier.offset(y = itemHeight / 2),
                    thickness = 1.dp,
                    color = indicatorColor
                )
            }
        }
        if (unitSuffix.isNotBlank()) {
            Text(
                text = unitSuffix,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = unitSuffixTextStyle),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}


// --- プレビュー関数群 ---

@Preview(showBackground = true, name = "プロフィール画面全体")
@Composable
fun GreetingPreview() {
    Myaku_rismuTheme {
        ProfileScreen()
    }
}

@Preview(showBackground = true, name = "基本情報アイテム (InfoItem)")
@Composable
fun InfoItemPreview() {
    Myaku_rismuTheme {
        Surface(modifier = Modifier.padding(8.dp)) {
            InfoItem(label = "テストラベル", value = "テスト値", onClick = {})
        }
    }
}

@Preview(showBackground = true, name = "選択可能情報フィールド (SelectableInfoField)")
@Composable
fun SelectableInfoFieldPreview() {
    Myaku_rismuTheme {
        Surface(modifier = Modifier.padding(8.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                SelectableInfoField(text = "クリック可能", onClick = {})
                SelectableInfoField(text = COMMON_PLACEHOLDER, onClick = {}) // ★ 共通プレースホルダー使用
                SelectableInfoField(text = "無効", onClick = {}, enabled = false)
            }
        }
    }
}

@Preview(showBackground = true, name = "身長ピッカー (100-220cm)")
@Composable
fun ModernNumberPickerDialogPreview_Height() {
    Myaku_rismuTheme {
        var showDialog by remember { mutableStateOf(true) }
        var selectedValue by remember { mutableStateOf<String?>("170") }

        if (showDialog) {
            ModernStringOrNumberPickerDialog(
                title = stringResource(R.string.select_height),
                options = (100..220).map { it.toString() },
                currentValue = selectedValue,
                onValueSelected = {
                    selectedValue = it
                    // showDialog = false // デバッグ中はコメントアウトでダイアログ維持
                },
                onDismiss = { showDialog = false },
                unitSuffix = "cm",
                unitSuffixTextStyle = defaultPickerTextStyleSelected.times(0.85f)
            )
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("選択された身長: ${selectedValue ?: "未選択"}${if (selectedValue != null) " cm" else ""}")
            Button(onClick = { showDialog = true }) {
                Text(if (selectedValue == null) stringResource(R.string.select_height) else "身長を変更 (${selectedValue}cm)")
            }
        }
    }
}

@Preview(showBackground = true, name = "体重ピッカー (30-150kg)")
@Composable
fun ModernNumberPickerDialogPreview_Weight_Min() {
    Myaku_rismuTheme {
        var showDialog by remember { mutableStateOf(true) }
        val weightOptions = remember { (30..150).map { it.toString() } }
        var selectedValue by remember { mutableStateOf<String?>(weightOptions[ (weightOptions.size / 2) ]) }

        if (showDialog) {
            ModernStringOrNumberPickerDialog(
                title = stringResource(R.string.select_weight),
                options = weightOptions,
                currentValue = selectedValue,
                onValueSelected = { selectedValue = it },
                onDismiss = { showDialog = false },
                unitSuffix = "kg",
                unitSuffixTextStyle = defaultPickerTextStyleSelected.times(0.85f)
            )
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("選択された体重: ${selectedValue ?: "未選択"}${if (selectedValue != null) " kg" else ""}")
            Button(onClick = { showDialog = true }) {
                Text(if (selectedValue == null) stringResource(R.string.select_weight) else "体重を変更 (${selectedValue}kg)")
            }
        }
    }
}


@Preview(showBackground = true, name = "性別ピッカー")
@Composable
fun ModernStringPickerDialogPreview_Gender() {
    Myaku_rismuTheme {
        var showDialog by remember { mutableStateOf(true) }
        var selectedValue by remember { mutableStateOf<String?>(genderDisplayOptions[0]) }

        if (showDialog) {
            ModernStringOrNumberPickerDialog(
                title = stringResource(R.string.select_gender),
                options = genderDisplayOptions,
                currentValue = selectedValue,
                onValueSelected = { selectedValue = it },
                onDismiss = { showDialog = false }
            )
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("選択された性別: ${selectedValue ?: "未選択"}")
            Button(onClick = { showDialog = true }) { Text(stringResource(R.string.select_gender)) }
        }
    }
}

@Preview(showBackground = true, name = "生年月日ピッカー")
@Composable
fun ModernBirthdatePickerDialogPreview() {
    Myaku_rismuTheme {
        var showDialog by remember { mutableStateOf(true) }
        val calendar = Calendar.getInstance()
        var year by remember { mutableIntStateOf(calendar.get(Calendar.YEAR) - 30) }
        var month by remember { mutableIntStateOf(calendar.get(Calendar.MONTH) + 1) }
        var day by remember { mutableIntStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

        if (showDialog) {
            ModernBirthdatePickerDialog(
                initialYear = year,
                initialMonth = month,
                initialDay = day,
                onBirthdateSelected = { y, m, d ->
                    year = y
                    month = m
                    day = d
                    // showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("選択された生年月日: ${year}年 ${month}月 ${day}日")
            Button(onClick = { showDialog = true }) { Text("生年月日を選択") }
        }
    }
}