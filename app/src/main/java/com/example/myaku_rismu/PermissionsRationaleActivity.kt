package com.example.myaku_rismu

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme
import android.webkit.WebResourceRequest
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.myaku_rismu.core.base.constants.PrivacyPolicy.PRIVACY_POLICY_URL
import com.example.myaku_rismu.ui.theme.customTheme

class PermissionsRationaleActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Myaku_rismuTheme {
                Surface(color = Color.White) {
                    PrivacyPolicyWebViewScreen(onBack = { finish() })
                }
            }
        }
    }
}

@Composable
fun PrivacyPolicyWebViewScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                val allowedUrl = PRIVACY_POLICY_URL
                                return request?.url.toString() != allowedUrl
                            }
                        }
                        settings.javaScriptEnabled = false
                        loadUrl(PRIVACY_POLICY_URL)
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        Button(
            onClick = onBack,
            shape = MaterialTheme.shapes.medium,
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.customTheme.bottomNavigationBarSelectedColor,
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .align(Alignment.End)
        ) {
            Text(
                text = "戻る",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
        }
    }
}