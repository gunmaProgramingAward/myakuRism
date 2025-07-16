package com.example.myaku_rismu.feature.setting

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.ui.TopBar
import com.example.myaku_rismu.feature.setting.components.ModernBirthdatePickerDialog
import com.example.myaku_rismu.feature.setting.components.ModernStringOrNumberPickerDialog
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme
import com.example.myaku_rismu.ui.theme.customTheme
import java.util.Calendar

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = viewModel(),
    appState: AppState
){
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    fun eventHandler(event: SettingUiEvent) {
        viewModel.onEvent(event)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.profile),
                navigationIcon = {
                    IconButton(onClick = { appState.navigatePopUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.top_bar_back_icon)
                        )
                    }
                },
                modifier = Modifier.background(MaterialTheme.customTheme.myakuRismuBackgroundColor),
            )
        },
        modifier = modifier
    ) { innerPadding ->
        SettingDetail(
            uiState = uiState,
            onEvent = { event -> eventHandler(event) },
            context = context,
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
        )
    }
}


@Composable
fun SettingDetail(
    modifier: Modifier = Modifier,
    uiState: SettingState,
    onEvent: (SettingUiEvent) -> Unit,
    context: Context = LocalContext.current
) {
    val commonPlaceholder = stringResource(R.string.not_set)
    val genderDisplayOptions = remember { context.resources.getStringArray(R.array.gender_display_options).toList() }
    val calendar = Calendar.getInstance()

    when (uiState.dialog) {
        SettingDialog.Birthdate -> {
            ModernBirthdatePickerDialog(
                initialYear = uiState.birthYear ?: (calendar.get(Calendar.YEAR) - 25),
                initialMonth = uiState.birthMonth ?: (calendar.get(Calendar.MONTH) + 1),
                initialDay = uiState.birthDay ?: calendar.get(Calendar.DAY_OF_MONTH),
                onBirthdateSelected = { year, month, day ->
                    onEvent(SettingUiEvent.BirthdateSelected(year, month, day))
                },
                onDismiss = { onEvent(SettingUiEvent.DismissDialog) }
            )
        }
        SettingDialog.Height -> {
            ModernStringOrNumberPickerDialog(
                title = stringResource(R.string.select_height),
                options = uiState.heightOptions,
                currentValue = uiState.heightCm?.toString(),
                onValueSelected = { selectedString ->
                    selectedString.toIntOrNull()?.let { onEvent(SettingUiEvent.HeightSelected(it)) }
                },
                onDismiss = { onEvent(SettingUiEvent.DismissDialog) },
                unitSuffix = stringResource(R.string.unit_of_height)
            )
        }
        SettingDialog.Weight -> {
            ModernStringOrNumberPickerDialog(
                title = stringResource(R.string.select_weight),
                options = uiState.weightOptions,
                currentValue = uiState.weightKg?.toString(),
                onValueSelected = { selectedString ->
                    selectedString.toIntOrNull()?.let { onEvent(SettingUiEvent.WeightSelected(it)) }
                },
                onDismiss = { onEvent(SettingUiEvent.DismissDialog) },
                unitSuffix = stringResource(R.string.unit_of_weight)
            )
        }
        SettingDialog.Gender -> {
            ModernStringOrNumberPickerDialog(
                title = stringResource(R.string.select_gender),
                options = genderDisplayOptions,
                currentValue = uiState.genderIndex?.let { genderDisplayOptions.getOrNull(it) },
                onValueSelected = { selectedString ->
                    onEvent(SettingUiEvent.GenderSelected(genderDisplayOptions.indexOf(selectedString)))
                },
                onDismiss = { onEvent(SettingUiEvent.DismissDialog) }
            )
        }
        null -> {}
    }
    // --- ダイアログ表示制御ここまで ---

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.customTheme.myakuRismuBackgroundColor),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ProfileCard(
            icon = Icons.Default.Person,
            text = stringResource(R.string.basic_information)
        ) {
            InfoItemLabel(
                onBirthdateClick = { onEvent(SettingUiEvent.ShowBirthdateDialog) },
                onHeightClick = { onEvent(SettingUiEvent.ShowHeightDialog) },
                onWeightClick = { onEvent(SettingUiEvent.ShowWeightDialog) },
                onGenderClick = { onEvent(SettingUiEvent.ShowGenderDialog) },
                commonPlaceholder = commonPlaceholder,
                genderDisplayOptions = genderDisplayOptions,
                uiState = uiState,
            )
        }
        ProfileCard(
            icon = Icons.Default.FavoriteBorder,
            text = stringResource(R.string.activity_level),
            contentBottomPadding = PaddingValues(bottom = 12.dp),
        ) {
            ActivityLevelLabel(
                context = context,
                selectedActivity = uiState.activityLevelIndex,
                onActivitySelected = { index ->
                    onEvent(SettingUiEvent.ActivityLevelSelected(index))
                }
            )
        }
    }
}


@Composable
private fun InfoItemLabel(
    modifier: Modifier = Modifier,
    uiState: SettingState,
    onBirthdateClick: () -> Unit,
    onHeightClick: () -> Unit,
    onWeightClick: () -> Unit,
    onGenderClick: () -> Unit,
    commonPlaceholder: String,
    genderDisplayOptions: List<String>,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        InfoItem(
            label = stringResource(R.string.date_of_birth),
            value = if (uiState.birthYear != null && uiState.birthMonth != null && uiState.birthDay != null) {
                stringResource(
                    R.string.date_format_jp,
                    uiState.birthYear,
                    uiState.birthMonth,
                    uiState.birthDay
                )
            } else commonPlaceholder,
            onClick = onBirthdateClick,
            isSelected = uiState.birthYear != null && uiState.birthMonth != null && uiState.birthDay != null,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(16.dp))
        InfoItem(
            label = stringResource(R.string.height),
            value = uiState.heightCm?.let { stringResource(R.string.height_display_format, it) }
                ?: commonPlaceholder,
            onClick = onHeightClick,
            isSelected = uiState.heightCm != null,
            modifier = Modifier.weight(1f)
        )
    }

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        InfoItem(
            label = stringResource(R.string.body_weight),
            value = uiState.weightKg?.let { stringResource(R.string.weight_display_format, it) }
                ?: commonPlaceholder,
            onClick = onWeightClick,
            isSelected = uiState.weightKg != null,
            modifier = Modifier.weight(1f)
        )
        Spacer(Modifier.width(16.dp))
        InfoItem(
            label = stringResource(R.string.gender),
            value = uiState.genderIndex?.let { genderDisplayOptions.getOrNull(it) } ?: commonPlaceholder,
            onClick = onGenderClick,
            isSelected = uiState.genderIndex != null,
            modifier = Modifier.weight(1f)
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
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(contentBottomPadding)
            ) {
                Icon(
                    icon,
                    contentDescription = null
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            content()
        }
    }
}


@Composable
private fun ActivityLevelLabel(
    modifier: Modifier = Modifier,
    context: Context,
    selectedActivity: Int,
    onActivitySelected: (Int) -> Unit
) {
    val activityLevels = remember {
        context.resources.getStringArray(R.array.activity_level_main_texts)
            .zip(context.resources.getStringArray(R.array.activity_level_sub_texts))
    }


    activityLevels.forEachIndexed { index, (mainText, subText) ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .shadow(2.dp, RoundedCornerShape(12.dp))
                .background(
                    Color.White,
                    RoundedCornerShape(12.dp)
                )
                .fillMaxWidth()
                .border(
                    width = if (selectedActivity == index) 1.5.dp else 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(12.dp)
                )
                .selectable(
                    selected = selectedActivity == index,
                    onClick = { onActivitySelected(index)},
                    role = Role.RadioButton
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = mainText,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = subText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.customTheme.settingScreenCommonColor
                )
            }
            RadioButton(
                selected = selectedActivity == index,
                onClick = { onActivitySelected(index) },
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.customTheme.bottomNavigationBarSelectedColor)
            )
        }
    }
}


@Composable
private fun InfoItem(
    label: String,
    value: String,
    onClick: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 13.sp),
            modifier = Modifier.padding(start = 2.dp, bottom = 2.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .border(
                    width = 1.dp,
                    brush = SolidColor(Color.Black),
                    shape = RoundedCornerShape(4.dp)
                )
                .clickable(
                    onClick = onClick,
                    role = Role.Button,
                )
                .padding(OutlinedTextFieldDefaults.contentPadding()),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = value,
                style = if (isSelected) {
                    MaterialTheme.typography.titleMedium
                } else {
                    MaterialTheme.typography.bodyMedium
                },
                color = if (isSelected) {
                    Color.Black
                } else {
                    MaterialTheme.customTheme.settingScreenCommonColor
                }
            )
        }
    }
}


@Preview(showBackground = true, name = "プロフィール画面全体")
@Composable
fun SettingScreenPreview() {
    val viewModel: SettingViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Myaku_rismuTheme {
        SettingDetail(
            uiState = uiState,
            context = context,
            onEvent = viewModel::onEvent,
        )
    }
}



