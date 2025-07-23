package com.example.myaku_rismu.core.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import com.example.myaku_rismu.core.rememberAppState
import com.example.myaku_rismu.ui.theme.DarkCustomTheme
import com.example.myaku_rismu.ui.theme.LightCustomTheme
import com.example.myaku_rismu.ui.theme.LocalCustomTheme
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        enableEdgeToEdge()

        setContent {
            val appState = rememberAppState()

            Myaku_rismuTheme {
                val darkTheme = isSystemInDarkTheme()
                val customTheme = if (darkTheme) DarkCustomTheme else LightCustomTheme

                CompositionLocalProvider(LocalCustomTheme provides customTheme) {
                    MaterialTheme {
                        MainAppScreen(
                            appState = appState,
                        )
                    }
                }
            }
        }
    }
}