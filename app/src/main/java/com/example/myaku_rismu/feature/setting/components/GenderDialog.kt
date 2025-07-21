package com.example.myaku_rismu.feature.setting.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.ui.dialog.ModernStringOrNumberPickerDialog
import com.example.myaku_rismu.feature.setting.SettingState
import com.example.myaku_rismu.feature.setting.SettingUiEvent

@Composable
fun GenderDialog(
    uiState: SettingState,
    eventHandler: (SettingUiEvent) -> Unit,
    context: Context
) {
    val genderDisplayOptions = remember { context.resources.getStringArray(R.array.gender_display_options).toList() }
    ModernStringOrNumberPickerDialog(
        title = stringResource(R.string.select_gender),
        options = genderDisplayOptions,
        currentValue = uiState.display.gender?.let { genderDisplayOptions.getOrNull(it) },
        onValueSelected = { selectedString ->
            eventHandler(SettingUiEvent.GenderSelected(genderDisplayOptions.indexOf(selectedString)))
        },
        onDismiss = { eventHandler(SettingUiEvent.DismissDialog) }
    )
}