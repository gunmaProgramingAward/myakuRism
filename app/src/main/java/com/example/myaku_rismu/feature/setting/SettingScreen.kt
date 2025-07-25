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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.ui.TopBar
import com.example.myaku_rismu.data.model.SettingType
import com.example.myaku_rismu.domain.model.ActivityLevel
import com.example.myaku_rismu.feature.setting.components.BirthdateDialog
import com.example.myaku_rismu.feature.setting.components.GenderDialog
import com.example.myaku_rismu.feature.setting.components.HeightDialog
import com.example.myaku_rismu.feature.setting.components.WeightDialog
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme
import com.example.myaku_rismu.ui.theme.customTheme


data class InfoItemData(
    val label: String,
    val value: String,
    val onClick: () -> Unit,
    val isSelected: Boolean
)

@Composable
fun SettingScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel(),
    appState: AppState
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    fun eventHandler(event: SettingUiEvent) {
        when (event) {
            is SettingUiEvent.DismissDialog -> {
                viewModel.dismissDialog()
            }

            is SettingUiEvent.ShowDialog -> {
                viewModel.showDialog(event.dialog)
            }

            is SettingUiEvent.HeightSelected -> {
                viewModel.selectHeight(event.height)
            }

            is SettingUiEvent.WeightSelected -> {
                viewModel.selectWeight(event.weight)
            }

            is SettingUiEvent.GenderSelected -> {
                viewModel.selectGender(event.gender)
            }

            is SettingUiEvent.BirthdateSelected -> {
                viewModel.selectBirthdate(event.year, event.month, event.day)
            }

            is SettingUiEvent.ActivityLevelSelected -> {
                viewModel.selectActivityLevel(event.level)
            }
        }
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
        DialogHandler(
            dialog = uiState.dialog,
            uiState = uiState,
            eventHandler = { event -> eventHandler(event) },
            context = context
        )
        SettingDetail(
            uiState = uiState,
            eventHandler = { event -> eventHandler(event) },
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
        )
    }
}

@Composable
fun DialogHandler(
    dialog: SettingType?,
    uiState: SettingState,
    eventHandler: (SettingUiEvent) -> Unit,
    context: Context
) {
    when (dialog) {
        SettingType.HEIGHT -> HeightDialog(uiState, eventHandler)
        SettingType.WEIGHT -> WeightDialog(uiState, eventHandler)
        SettingType.GENDER -> GenderDialog(uiState, eventHandler, context)
        SettingType.BIRTHDATE -> BirthdateDialog(uiState, eventHandler)
        null -> Unit
    }
}

@Composable
fun SettingDetail(
    modifier: Modifier = Modifier,
    uiState: SettingState,
    eventHandler: (SettingUiEvent) -> Unit,
) {
    val commonPlaceholder = stringResource(R.string.not_set)

    val infoItems = listOf(
        InfoItemData(
            label = stringResource(R.string.date_of_birth),
            value = if (uiState.display.birthYear != null && uiState.display.birthMonth != null && uiState.display.birthDay != null) {
                stringResource(
                    R.string.date_format_jp,
                    uiState.display.birthYear,
                    uiState.display.birthMonth,
                    uiState.display.birthDay
                )
            } else commonPlaceholder,
            onClick = { eventHandler(SettingUiEvent.ShowDialog(SettingType.BIRTHDATE)) },
            isSelected = uiState.display.birthYear != null && uiState.display.birthMonth != null && uiState.display.birthDay != null
        ),
        InfoItemData(
            label = stringResource(R.string.height),
            value = uiState.display.heightCm?.let {
                stringResource(
                    R.string.height_display_format,
                    it
                )
            }
                ?: commonPlaceholder,
            onClick = { eventHandler(SettingUiEvent.ShowDialog(SettingType.HEIGHT)) },
            isSelected = uiState.display.heightCm != null
        ),
        InfoItemData(
            label = stringResource(R.string.body_weight),
            value = uiState.display.weightKg?.let {
                stringResource(
                    R.string.weight_display_format,
                    it
                )
            }
                ?: commonPlaceholder,
            onClick = { eventHandler(SettingUiEvent.ShowDialog(SettingType.WEIGHT)) },
            isSelected = uiState.display.weightKg != null
        ),
        InfoItemData(
            label = stringResource(R.string.gender),
            value = uiState.display.gender?.let { stringResource(it.displayName) }
                ?: commonPlaceholder,
            onClick = { eventHandler(SettingUiEvent.ShowDialog(SettingType.GENDER)) },
            isSelected = uiState.display.gender != null
        )
    )

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
                items = infoItems
            )
        }
        ProfileCard(
            icon = Icons.Default.FavoriteBorder,
            text = stringResource(R.string.activity_level),
            contentBottomPadding = PaddingValues(bottom = 12.dp),
        ) {
            ActivityLevelLabel(
                selectedActivity = uiState.display.activityLevel ?: ActivityLevel.LOW,
                onActivitySelected = { level ->
                    eventHandler(SettingUiEvent.ActivityLevelSelected(level))
                }
            )
        }
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
        shape = RoundedCornerShape(5.dp),
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.customTheme.myakuRismuCardColor
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

@Composable
private fun InfoItemLabel(
    modifier: Modifier = Modifier,
    items: List<InfoItemData>
) {
    items.chunked(2).forEach { rowItems ->
        Row(modifier = modifier.fillMaxWidth()) {
            rowItems.forEach { item ->
                InfoItem(
                    label = item.label,
                    value = item.value,
                    onClick = item.onClick,
                    isSelected = item.isSelected,
                    modifier = Modifier.weight(1f)
                )
                if (item != rowItems.last()) Spacer(Modifier.width(16.dp))
            }
        }
    }
}


@Composable
private fun ActivityLevelLabel(
    modifier: Modifier = Modifier,
    selectedActivity: ActivityLevel,
    onActivitySelected: (ActivityLevel) -> Unit
) {
    ActivityLevel.entries.forEach { level ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .shadow(2.dp, RoundedCornerShape(12.dp))
                .background(MaterialTheme.customTheme.myakuRismuCardColor, RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .border(
                    width = if (selectedActivity == level) 1.5.dp else 1.dp,
                    color = Color.Black,
                    shape = RoundedCornerShape(12.dp)
                )
                .selectable(
                    selected = selectedActivity == level,
                    onClick = { onActivitySelected(level) },
                    role = Role.RadioButton
                )
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = stringResource(id = level.mainTextRes),
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = stringResource(id = level.subTextRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.customTheme.settingScreenCommonColor
                )
            }
            RadioButton(
                selected = selectedActivity == level,
                onClick = { onActivitySelected(level) },
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.customTheme.bottomNavigationBarSelectedColor
                )
            )
        }
    }
}


@Preview(showBackground = true, name = "プロフィール画面全体")
@Composable
fun SettingScreenPreview() {
    Myaku_rismuTheme {
        SettingDetail(
            uiState = SettingState(), // ダミーState
            eventHandler = {},
        )
    }
}



