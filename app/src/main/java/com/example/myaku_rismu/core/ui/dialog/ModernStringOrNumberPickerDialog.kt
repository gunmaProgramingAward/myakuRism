package com.example.myaku_rismu.core.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myaku_rismu.R
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme
import com.example.myaku_rismu.ui.theme.customTheme
import java.util.Calendar
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow

private val defaultPickerItemHeight = 48.dp
private const val defaultPickerVisibleItemsCount = 5
private val defaultPickerTextStyleNormal = 20.sp
private val defaultPickerTextStyleSelected = 28.sp


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
        if (idx != -1) idx else (options.size / 2)
    }

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
        shape = RoundedCornerShape(8.dp),
        containerColor = Color.White,
        title = {
            Text(
                title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.headlineLarge,
            )
        },
        text = {
            Box(
                modifier = Modifier
                    .height(totalLayoutHeight)
            ) {
                LazyColumn(
                    state = listState,
                    flingBehavior = snappingBehavior,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(halfVisibleItems) {
                        Spacer(modifier = Modifier.height(itemHeight))
                    }

                    items(
                        options.size,
                        key = { index -> options[index].hashCode()}) { optionIndex ->
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
                                .padding(vertical = 2.dp)
                                .graphicsLayer {
                                    if (visibleItemsCount > 1) {
                                        scaleX = if (isSelected) 1f else scaleFactor
                                        scaleY = if (isSelected) 1f else scaleFactor
                                        alpha =
                                            if (isSelected) 1f else alphaFactor.pow(1.2f)
                                    }
                                    transformOrigin = TransformOrigin.Center
                                }
                        ) {
                            Text(
                                text = optionValue,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = if (isSelected) textStyleSelected else textStyleNormal,
                                    color = if (isSelected) Color.Black
                                    else MaterialTheme.customTheme.settingScreenNormalTextColor
                                )
                            )
                        }
                    }
                    items(halfVisibleItems) {
                        Spacer(modifier = Modifier.height(itemHeight))
                    }
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(0.5f)
                        .height(48.dp)
                        .background(
                            color = MaterialTheme.customTheme.onSelectedButtonOverlay,
                            shape = RoundedCornerShape(16.dp)
                        )
                )
                Text(
                    text = unitSuffix,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 24.sp),
                    color = Color.Black,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 14.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (centralVisibleOptionIndex < options.size && options.isNotEmpty()) {
                        onValueSelected(options[centralVisibleOptionIndex])
                    }
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.customTheme.bottomNavigationBarSelectedColor,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.ok),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.customTheme.bottomNavigationBarSelectedColor
                    )
                )
            }
        }
    )
}


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
        shape = RoundedCornerShape(8.dp),
        containerColor = Color.White,
        title = {
            Text(
                stringResource(R.string.select_your_date_of_birth),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineLarge,
            )
        },
        text = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                ModernPickerColumnInternal(
                    options = yearOptions,
                    currentValue = tempYear.toString(),
                    onValueSelected = { tempYear = it.toInt() },
                    modifier = Modifier.weight(1.3f),
                    unitSuffix = stringResource(R.string.year),
                    itemHeight = defaultPickerItemHeight,
                    visibleItemsCount = defaultPickerVisibleItemsCount
                )
                ModernPickerColumnInternal(
                    options = monthOptions,
                    currentValue = tempMonth.toString(),
                    onValueSelected = { tempMonth = it.toInt() },
                    modifier = Modifier.weight(1f),
                    unitSuffix = stringResource(R.string.month),
                    itemHeight = defaultPickerItemHeight,
                    visibleItemsCount = defaultPickerVisibleItemsCount
                )
                ModernPickerColumnInternal(
                    options = dayOptions,
                    currentValue = tempDay.toString(),
                    onValueSelected = { tempDay = it.toInt() },
                    modifier = Modifier.weight(1f),
                    unitSuffix = stringResource(R.string.day),
                    itemHeight = defaultPickerItemHeight,
                    visibleItemsCount = defaultPickerVisibleItemsCount
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onBirthdateSelected(tempYear, tempMonth, tempDay)
                    onDismiss()

                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.customTheme.bottomNavigationBarSelectedColor,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = stringResource(R.string.ok),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.customTheme.bottomNavigationBarSelectedColor
                    )
                )
            }
        }
    )
}


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
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = if (isSelected) textStyleSelected else textStyleNormal,
                                color = if (isSelected) Color.Black
                                else MaterialTheme.customTheme.settingScreenNormalTextColor
                            )
                        )
                    }
                }
                items(halfVisibleItems) { Spacer(modifier = Modifier.height(itemHeight)) }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .align(Alignment.Center)
                    .height(48.dp)
                    .background(
                        color = MaterialTheme.customTheme.onSelectedButtonOverlay,
                        shape = RoundedCornerShape(16.dp)
                    )
            )
        }
            Text(
                text = unitSuffix,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
                modifier = Modifier.padding(top = 2.dp)
            )
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
                unitSuffix = "cm"
            )
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
        ) {

        }
    }
}


@Preview(showBackground = true, name = "体重ピッカー (30-150kg)")
@Composable
fun ModernNumberPickerDialogPreview_Weight_Min() {
    Myaku_rismuTheme {
        var showDialog by remember { mutableStateOf(true) }
        val weightOptions = remember { (30..150).map { it.toString() } }
        var selectedValue by remember { mutableStateOf<String?>(weightOptions[(weightOptions.size / 2)]) }

        if (showDialog) {
            ModernStringOrNumberPickerDialog(
                title = stringResource(R.string.select_weight),
                options = weightOptions,
                currentValue = selectedValue,
                onValueSelected = { selectedValue = it },
                onDismiss = { showDialog = false },
                unitSuffix = "kg"
            )
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
        }
    }
}


@Preview(showBackground = true, name = "性別ピッカー")
@Composable
fun ModernStringPickerDialogPreview_Gender() {
    Myaku_rismuTheme {
        var showDialog by remember { mutableStateOf(true) }
        var selectedValue by remember {
            mutableStateOf<String?>(
                listOf(
                    "男性",
                    "女性",
                    "その他",
                    "回答しない"
                )[0]
            )
        }

        if (showDialog) {
            ModernStringOrNumberPickerDialog(
                title = stringResource(R.string.select_gender),
                options = listOf("男性", "女性", "その他", "回答しない"),
                currentValue = selectedValue,
                onValueSelected = { selectedValue = it },
                onDismiss = { showDialog = false }
            )
        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {}
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
        ) {
        }
    }
}