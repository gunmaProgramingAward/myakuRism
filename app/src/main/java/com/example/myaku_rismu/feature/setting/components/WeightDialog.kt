package com.example.myaku_rismu.feature.setting.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.ui.dialog.ModernStringOrNumberPickerDialog
import com.example.myaku_rismu.feature.setting.SettingState
import com.example.myaku_rismu.feature.setting.SettingUiEvent

@Composable
fun WeightDialog(
    uiState: SettingState,
    eventHandler: (SettingUiEvent) -> Unit
) {
    val weightOptions: List<String> = (0..200).map { it.toString() }
    ModernStringOrNumberPickerDialog(
        title = stringResource(R.string.select_weight),
        options = weightOptions,
        currentValue = uiState.display.weightKg?.toString(),
        onValueSelected = { selectedString ->
            selectedString.toIntOrNull()?.let { eventHandler(SettingUiEvent.WeightSelected(it)) }
        },
        onDismiss = { eventHandler(SettingUiEvent.DismissDialog) },
        unitSuffix = stringResource(R.string.unit_of_weight)
    )
}