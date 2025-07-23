package com.example.myaku_rismu.core.ui.dialog

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HealthConnectUnavailableDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    // TODO: 後ほどUIのデザインシステムを統一したダイアログを実装
    AppDialog(
        title = "Health Connectが利用できません",
        message = "この端末ではHealth Connectが利用できません。\n未インストールの場合はGoogle Playからダウンロードしてください。",
        icon = {
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                tint = Color(0xFFFF4AA3),
                modifier = Modifier.size(48.dp)
            )
        },
        confirmText = "Google Playを開く",
        dismissText = "閉じる",
        onConfirm = { onConfirm() },
        onDismiss = { onDismiss() },
        accentColor = Color(0xFF000000)
    )
}

@Preview
@Composable
fun HealthConnectUnavailableDialogPreview() {
    MaterialTheme {
        HealthConnectUnavailableDialog(
            onConfirm = {},
            onDismiss = {},
        )
    }
}