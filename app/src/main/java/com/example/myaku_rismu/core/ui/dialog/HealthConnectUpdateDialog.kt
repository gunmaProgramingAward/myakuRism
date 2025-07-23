package com.example.myaku_rismu.core.ui.dialog

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HealthConnectUpdateDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    // TODO: 後ほどUIのデザインシステムを統一したダイアログを実装
    AppDialog(
        title = "Health Connectのアップデートが必要です",
        message = "最新バージョンのHealth Connectが必要です。Google Playストアからアップデートしてください。",
        confirmText = "アップデートへ",
        icon = {
            Icon(
                Icons.Default.Build,
                contentDescription = null,
                tint = Color(0xFF28A1E2),
                modifier = Modifier.size(48.dp)
            )
        },
        onConfirm = { onConfirm() },
        dismissText = "閉じる",
        onDismiss = onDismiss,
        accentColor = Color(0xFF000000)
    )
}

@Preview
@Composable
fun HealthConnectUpdateDialogPreview() {
    MaterialTheme {
        HealthConnectUpdateDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}