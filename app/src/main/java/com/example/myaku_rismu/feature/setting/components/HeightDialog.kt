package com.example.myaku_rismu.feature.setting.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.ui.dialog.VerticalWheelPickerDialog
import com.example.myaku_rismu.feature.setting.SettingState
import com.example.myaku_rismu.feature.setting.SettingUiEvent

@Composable
fun HeightDialog(
    uiState: SettingState,
    eventHandler: (SettingUiEvent) -> Unit
) {
    val heightOptions: List<String> = (0..300).map { it.toString() }
    VerticalWheelPickerDialog(
        title = stringResource(R.string.select_height),
        options = heightOptions,
        currentValue = uiState.display.heightCm?.toString(),
        onValueSelected = { selectedString ->
            selectedString.toIntOrNull()?.let { eventHandler(SettingUiEvent.HeightSelected(it)) }
        },
        onDismiss = { eventHandler(SettingUiEvent.DismissDialog) },
        unitSuffix = stringResource(R.string.unit_of_height)
    )
}