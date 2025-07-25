package com.example.myaku_rismu.feature.setting.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.ui.dialog.ModernStringOrNumberPickerDialog
import com.example.myaku_rismu.domain.model.Gender
import com.example.myaku_rismu.feature.setting.SettingState
import com.example.myaku_rismu.feature.setting.SettingUiEvent


@Composable
fun GenderDialog(
    uiState: SettingState,
    eventHandler: (SettingUiEvent) -> Unit,
    context: Context
) {
    val options = Gender.entries.map { context.getString(it.displayName) }
    ModernStringOrNumberPickerDialog(
        title = context.getString(R.string.select_gender),
        options = options,
        currentValue = uiState.display.gender?.let { context.getString(it.displayName) },
        onValueSelected = { selectedString ->
            Gender.entries.find { context.getString(it.displayName) == selectedString }
                ?.let { eventHandler(SettingUiEvent.GenderSelected(it)) }
        },
        onDismiss = { eventHandler(SettingUiEvent.DismissDialog) }
    )
}