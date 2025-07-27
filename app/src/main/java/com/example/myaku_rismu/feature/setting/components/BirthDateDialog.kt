package com.example.myaku_rismu.feature.setting.components

import androidx.compose.runtime.Composable
import com.example.myaku_rismu.core.ui.dialog.ModernBirthdatePickerDialog
import com.example.myaku_rismu.feature.setting.SettingState
import com.example.myaku_rismu.feature.setting.SettingUiEvent
import java.util.Calendar

@Composable
fun BirthdateDialog(
    uiState: SettingState,
    eventHandler: (SettingUiEvent) -> Unit
) {
    val calendar = Calendar.getInstance()
    ModernBirthdatePickerDialog(
        initialYear = uiState.display.birthYear ?: (calendar.get(Calendar.YEAR) - 25),
        initialMonth = uiState.display.birthMonth ?: (calendar.get(Calendar.MONTH) + 1),
        initialDay = uiState.display.birthDay ?: calendar.get(Calendar.DAY_OF_MONTH),
        onBirthdateSelected = { year, month, day ->
            eventHandler(SettingUiEvent.BirthdateSelected(year, month, day))
        },
        onDismiss = { eventHandler(SettingUiEvent.DismissDialog) }
    )
}