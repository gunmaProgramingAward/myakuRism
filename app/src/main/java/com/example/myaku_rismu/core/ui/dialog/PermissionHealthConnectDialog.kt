package com.example.myaku_rismu.core.ui.dialog

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme

@Composable
fun PermissionHealthConnectDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    // TODO: 後ほどUIのデザインシステムを統一したダイアログを実装
    AppDialog(
        title = "権限が必要です",
        message = "この機能を利用するにはHealth Connectの権限が必要です。\n設定アプリから『Health Connect』→『アプリの権限』→『（本アプリ名）』で権限をONにしてください。",
        icon = { Icon(
            Icons.Default.Lock,
            contentDescription = null,
            tint = Color(0xFFFFC100),
            modifier = Modifier.size(48.dp)
        ) },
        confirmText = "設定を開く",
        onConfirm = { onConfirm() },
        dismissText = "閉じる",
        onDismiss = { onDismiss() },
        accentColor = Color(0xFF000000)
    )
}

@Preview
@Composable
fun PermissionHealthConnectDialogPreview() {
    Myaku_rismuTheme {
        PermissionHealthConnectDialog(
            onConfirm = {},
            onDismiss = {}
        )
    }
}