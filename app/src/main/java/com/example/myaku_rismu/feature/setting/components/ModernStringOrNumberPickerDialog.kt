package com.example.myaku_rismu.feature.setting.components

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow

private val defaultPickerItemHeight = 48.dp
private const val defaultPickerVisibleItemsCount = 5
private val defaultPickerTextStyleNormal = 20.sp
private val defaultPickerTextStyleSelected = 28.sp

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
    unitSuffixTextStyle: TextUnit = defaultPickerTextStyleSelected.times(0.85f)
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
    }.coerceIn(0, max(0, options.size - 1))

    val scrollToInitialIndex = actualInitialIndex + halfVisibleItems
    val totalLayoutHeight = itemHeight * visibleItemsCount

    val centralVisibleOptionIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty() || options.isEmpty()) {
                actualInitialIndex
            } else {
                val viewportCenterY =
                    layoutInfo.viewportStartOffset + layoutInfo.viewportSize.height / 2
                layoutInfo.visibleItemsInfo
                    .filter { it.index >= halfVisibleItems && it.index < options.size + halfVisibleItems }
                    .minByOrNull { abs((it.offset + it.size / 2) - viewportCenterY) }
                    ?.let { it.index - halfVisibleItems }
                    ?.coerceIn(0, max(0, options.size - 1))
                    ?: actualInitialIndex
            }
        }
    }

    LaunchedEffect(
        listState,
        scrollToInitialIndex,
        options.size,
        itemHeight,
        totalLayoutHeight,
        density
    ) {
        if (options.isNotEmpty() && scrollToInitialIndex < listState.layoutInfo.totalItemsCount) {
            val targetOffset =
                (with(density) { totalLayoutHeight.toPx() / 2 - itemHeight.toPx() / 2 }).toInt()
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

                            items(
                                options.size,
                                key = { index -> options[index] + "_picker_item" }) { optionIndex ->
                                val optionValue = options[optionIndex]
                                val isSelected = optionIndex == centralVisibleOptionIndex

                                val distanceToCenterNormalizedAbs =
                                    abs(optionIndex - centralVisibleOptionIndex)
                                val scaleFactor =
                                    ((halfVisibleItems - distanceToCenterNormalizedAbs).toFloat() / halfVisibleItems * 0.25f + 0.75f)
                                        .coerceIn(0.75f, 1f)
                                val alphaFactor =
                                    ((halfVisibleItems - distanceToCenterNormalizedAbs).toFloat() / halfVisibleItems * 0.6f + 0.4f)
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
                                                    color = MaterialTheme.colorScheme.primary.copy(
                                                        alpha = 0.85f
                                                    )
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
                            val indicatorColor =
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
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
            TextButton(onClick = onDismiss) {
                Text(
                    "キャンセル",
                    color = MaterialTheme.colorScheme.primary
                )
            }
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
            TextButton(onClick = onDismiss) {
                Text(
                    "キャンセル",
                    color = MaterialTheme.colorScheme.primary
                )
            }
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
    }.coerceIn(0, max(0, options.size - 1))

    val scrollToInitialIndex = actualInitialIndex + halfVisibleItems
    val totalLayoutHeight = itemHeight * visibleItemsCount

    val centralVisibleOptionIndex by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty() || options.isEmpty()) {
                actualInitialIndex
            } else {
                val viewportCenterY =
                    layoutInfo.viewportStartOffset + layoutInfo.viewportSize.height / 2
                layoutInfo.visibleItemsInfo
                    .filter { it.index >= halfVisibleItems && it.index < options.size + halfVisibleItems }
                    .minByOrNull { abs((it.offset + it.size / 2) - viewportCenterY) }
                    ?.let { it.index - halfVisibleItems }
                    ?.coerceIn(0, max(0, options.size - 1))
                    ?: actualInitialIndex
            }
        }
    }

    LaunchedEffect(
        listState,
        scrollToInitialIndex,
        options.size,
        itemHeight,
        totalLayoutHeight,
        density
    ) {
        if (options.isNotEmpty() && scrollToInitialIndex < listState.layoutInfo.totalItemsCount) {
            val targetOffset =
                (with(density) { totalLayoutHeight.toPx() / 2 - itemHeight.toPx() / 2 }).toInt()
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

                items(
                    options.size,
                    key = { index -> options[index] + "_col_item" }) { optionIndex ->
                    val optionValue = options[optionIndex]
                    val isSelected = optionIndex == centralVisibleOptionIndex

                    val distanceToCenterNormalizedAbs = abs(optionIndex - centralVisibleOptionIndex)
                    val scaleFactor =
                        ((halfVisibleItems - distanceToCenterNormalizedAbs).toFloat() / halfVisibleItems * 0.2f + 0.8f)
                            .coerceIn(0.8f, 1f)
                    val alphaFactor =
                        ((halfVisibleItems - distanceToCenterNormalizedAbs).toFloat() / halfVisibleItems * 0.5f + 0.5f)
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