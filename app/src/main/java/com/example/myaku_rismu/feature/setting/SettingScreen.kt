package com.example.myaku_rismu.feature.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.ui.dialog.ModernBirthdatePickerDialog
import com.example.myaku_rismu.core.ui.dialog.VerticalWheelPickerDialog
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme
import com.example.myaku_rismu.ui.theme.customTheme
import java.util.Calendar


@Composable
fun SettingScreen(
//    appState: AppState,
    modifier: Modifier = Modifier
) {
    // --- 定数 ---
    val commonPlaceholder = stringResource(R.string.not_set)
    val unSetValue = -1 // 未設定を示す整数値

    val resources = LocalContext.current.resources
    val genderDisplayOptions = remember { resources.getStringArray(R.array.gender_display_options).toList() }

    val calendar = Calendar.getInstance()

    // --- State 変数　---
    var selectedYear by remember { mutableStateOf<Int?>(null) }
    var selectedMonth by remember { mutableStateOf<Int?>(null) }
    var selectedDay by remember { mutableStateOf<Int?>(null) }

    var currentHeightCm by remember { mutableStateOf<Int?>(null) }
    var currentWeightKg by remember { mutableStateOf<Int?>(null) }
    var selectedGenderIndex by remember { mutableIntStateOf(unSetValue) }

    var showBirthdateDialog by remember { mutableStateOf(false) }
    var showHeightDialog by remember { mutableStateOf(false) }
    var showWeightDialog by remember { mutableStateOf(false) }
    var showGenderDialog by remember { mutableStateOf(false) }

    val birthDateInteractionSource = remember { MutableInteractionSource() }
    val heightInteractionSource = remember { MutableInteractionSource() }
    val weightInteractionSource = remember { MutableInteractionSource() }
    val genderInteractionSource = remember { MutableInteractionSource() }

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
        VerticalWheelPickerDialog(
            title = stringResource(R.string.select_height),
            options = (100..220).map { it.toString() },
            currentValue = currentHeightCm?.toString(),
            onValueSelected = { selectedString ->
                currentHeightCm = selectedString.toIntOrNull()
                showHeightDialog = false
            },
            onDismiss = { showHeightDialog = false },
            unitSuffix = stringResource(R.string.unit_of_height)
        )
    }

    if (showWeightDialog) {
        VerticalWheelPickerDialog(
            title = stringResource(R.string.select_weight),
            options = remember { (30..150).map { it.toString() } },
            currentValue = currentWeightKg?.toString(),
            onValueSelected = { selectedString ->
                currentWeightKg = selectedString.toIntOrNull()
                showWeightDialog = false
            },
            onDismiss = { showWeightDialog = false },
            unitSuffix = stringResource(R.string.unit_of_weight)
        )
    }

    if (showGenderDialog) {
        VerticalWheelPickerDialog(
            title = stringResource(R.string.select_gender),
            options = genderDisplayOptions,
            currentValue = if (selectedGenderIndex != unSetValue && selectedGenderIndex in genderDisplayOptions.indices) {
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

    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.customTheme.myakuRismuBackgroundColor,
        contentColor = Color.Black
    ) {
        Column(
            modifier = modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            TopTitleAndBackButton(
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                text = stringResource(R.string.profile)
            )
            ProfileCard(
                icon = Icons.Default.Person,
                text = stringResource(R.string.basic_information)
            ) {
                InfoItemLabel(
                    selectedYear = selectedYear,
                    selectedMonth = selectedMonth,
                    selectedDay = selectedDay,
                    currentHeightCm = currentHeightCm,
                    currentWeightKg = currentWeightKg,
                    selectedGenderIndex = selectedGenderIndex,
                    onBirthdateClick = { showBirthdateDialog = true },
                    onHeightClick = { showHeightDialog = true },
                    onWeightClick = { showWeightDialog = true },
                    onGenderClick = { showGenderDialog = true },
                    commonPlaceholder = commonPlaceholder,
                    genderDisplayOptions = genderDisplayOptions,
                    unSetValue = unSetValue,
                    birthDateInteractionSource = birthDateInteractionSource,
                    heightInteractionSource = heightInteractionSource,
                    weightInteractionSource = weightInteractionSource,
                    genderInteractionSource = genderInteractionSource
                )
            }
            ProfileCard(
                icon = Icons.Default.FavoriteBorder,
                text = stringResource(R.string.activity_level),
                contentBottomPadding = PaddingValues(bottom = 12.dp)
            ) { ActivityLevelLabel() }
        }
    }
}



@Composable
private fun InfoItemLabel(
    modifier: Modifier = Modifier,
    selectedYear: Int?,
    selectedMonth: Int?,
    selectedDay: Int?,
    currentHeightCm: Int?,
    currentWeightKg: Int?,
    selectedGenderIndex: Int,
    onBirthdateClick: () -> Unit,
    onHeightClick: () -> Unit,
    onWeightClick: () -> Unit,
    onGenderClick: () -> Unit,
    commonPlaceholder: String,
    unSetValue: Int,
    genderDisplayOptions: List<String>,
    birthDateInteractionSource: MutableInteractionSource,
    heightInteractionSource: MutableInteractionSource,
    weightInteractionSource: MutableInteractionSource,
    genderInteractionSource: MutableInteractionSource
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = modifier.weight(1f)) {
            InfoItem(
                label = stringResource(R.string.date_of_birth),
                value = if (selectedYear != null && selectedMonth != null && selectedDay != null) {
                    stringResource(
                        R.string.date_format_jp,
                        selectedYear,
                        selectedMonth,
                        selectedDay
                    )
                } else commonPlaceholder,
                onClick = onBirthdateClick,
                interactionSource = birthDateInteractionSource
            )
        }
        Box(modifier = modifier.weight(1f)) {
            InfoItem(
                label = stringResource(R.string.height),
                value = currentHeightCm?.let { stringResource(R.string.height_display_format, it) } ?: commonPlaceholder,
                onClick = onHeightClick,
                interactionSource = heightInteractionSource
            )
        }
    }

    Spacer(Modifier.height(8.dp))
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(modifier = modifier.weight(1f)) {
            InfoItem(
                label = stringResource(R.string.body_weight),
                value = currentWeightKg?.let { stringResource(R.string.weight_display_format, it) } ?: commonPlaceholder,
                onClick = onWeightClick,
                interactionSource = weightInteractionSource
            )
        }
        Box(modifier = modifier.weight(1f)) {
            InfoItem(
                label = stringResource(R.string.gender),
                value = if (selectedGenderIndex != unSetValue && selectedGenderIndex in genderDisplayOptions.indices) {
                    genderDisplayOptions[selectedGenderIndex]
                } else commonPlaceholder,
                onClick = onGenderClick,
                interactionSource = genderInteractionSource
            )
        }
    }
}


@Composable
private fun TopTitleAndBackButton(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(top = (8.dp), bottom = (12.dp))
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = modifier
                .padding(top = 1.dp)
                .size(20.dp)
        )
        Spacer(modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 20.sp,
                lineHeight = 28.sp
            )
        )
    }
}


@Composable
private fun ProfileCard(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    contentBottomPadding: PaddingValues = PaddingValues(bottom = 8.dp),
    content: @Composable () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.customTheme.myakuRismuCardColor
        )
    ) {
        Column(modifier = modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.padding(contentBottomPadding)
            ) {
                Icon(
                    icon,
                    contentDescription = null
                )
                Spacer(modifier.width(4.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            content()
        }
    }
}


@Composable
private fun ActivityLevelLabel(modifier: Modifier = Modifier) {
    val resources = LocalContext.current.resources
    val activityLevelMainTexts = remember { resources.getStringArray(R.array.activity_level_main_texts).toList() }
    val activityLevelSubTexts = remember { resources.getStringArray(R.array.activity_level_sub_texts).toList() }
    val activityLevels = remember(activityLevelMainTexts, activityLevelSubTexts) {
        activityLevelMainTexts.zip(activityLevelSubTexts)
    }

    var selectedActivity by remember { mutableIntStateOf(0) }

    activityLevels.forEachIndexed { index, (mainText, subText) ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = if (selectedActivity == index) MaterialTheme.customTheme.onSelectedButtonOverlay
                    else Color.Transparent,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = 1.5.dp,
                    color = if (selectedActivity == index) MaterialTheme.colorScheme.primary
                    else Color.Black,
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
            Column(
                modifier = modifier.weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = mainText,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = subText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.customTheme.settingScreenCommonColor
                )
            }
            RadioButton(
                selected = selectedActivity == index,
                onClick = { selectedActivity = index },
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
            )
        }
        if (index < activityLevels.size - 1) {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
private fun InfoItem(
    label: String,
    value: String,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 13.sp),
            modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)
        )
        SelectableInfoField(
            text = value,
            onClick = onClick,
            interactionSource = interactionSource
        )
    }
}


@Composable
private fun SelectableInfoField(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(
                minWidth = OutlinedTextFieldDefaults.MinWidth,
                minHeight = OutlinedTextFieldDefaults.MinHeight
            )
            .border(
                width = OutlinedTextFieldDefaults.UnfocusedBorderThickness,
                brush = SolidColor(Color.Black),
                shape = OutlinedTextFieldDefaults.shape
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
            color = if (enabled) {
                MaterialTheme.customTheme.settingScreenCommonColor
            } else {
                Color.Black
            },
            textAlign = TextAlign.Start
        )
    }
}

// --- プレビュー関数群 ---

@Preview(showBackground = true, name = "プロフィール画面全体")
@Composable
fun SettingScreenPreview() {
    Myaku_rismuTheme {
        SettingScreen()
    }
}


@Preview(showBackground = true, name = "身長ピッカー (100-220cm)")
@Composable
fun ModernNumberPickerDialogPreview_Height() {
    Myaku_rismuTheme {
        var showDialog by remember { mutableStateOf(true) }
        var selectedValue by remember { mutableStateOf<String?>("170") }

        if (showDialog) {
            VerticalWheelPickerDialog(
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
        var selectedValue by remember { mutableStateOf<String?>(weightOptions[(weightOptions.size / 2)]) }

        if (showDialog) {
            VerticalWheelPickerDialog(
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
        var selectedValue by remember { mutableStateOf<String?>(listOf("男性", "女性", "その他", "回答しない")[0]) }

        if (showDialog) {
            VerticalWheelPickerDialog(
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
