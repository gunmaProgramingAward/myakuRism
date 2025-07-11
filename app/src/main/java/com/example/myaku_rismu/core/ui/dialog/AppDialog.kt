package com.example.myaku_rismu.core.ui.dialog

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myaku_rismu.ui.theme.customTheme


@Composable
fun AppDialog(
    title: String,
    message: String,
    icon: @Composable (() -> Unit)? = null,
    confirmText: String = "OK",
    onConfirm: () -> Unit,
    dismissText: String? = null,
    onDismiss: (() -> Unit)? = null,
    accentColor: Color = MaterialTheme.customTheme.bottomNavigationBarSelectedColor
) {
    // TODO: 後ほどUIのデザインシステムを統一したダイアログを実装
    AlertDialog(
        onDismissRequest = { onDismiss?.invoke() },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        icon = icon,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF474747),
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = confirmText,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        dismissButton = {
            if (dismissText != null && onDismiss != null) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = dismissText,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF474747)
                    )
                }
            }
        }
    )
}

@Preview
@Composable
fun AppDialogPreview() {
    AppDialog(
        title = "ダイアログのタイトル",
        message = "これはダイアログのメッセージです。",
        confirmText = "確認",
        onConfirm = {},
        dismissText = "キャンセル",
        onDismiss = {}
    )
}