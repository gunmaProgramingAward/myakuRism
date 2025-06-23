package com.example.myaku_rismu.core.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myaku_rismu.feature.musicDetail.MusicDetailScreen
import com.example.myaku_rismu.ui.theme.DarkCustomTheme
import com.example.myaku_rismu.ui.theme.LightCustomTheme
import com.example.myaku_rismu.ui.theme.LocalCustomTheme
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Myaku_rismuTheme {
                val darkTheme = isSystemInDarkTheme()
                val customTheme = if (darkTheme) DarkCustomTheme else LightCustomTheme

                CompositionLocalProvider(LocalCustomTheme provides customTheme) {
                    MaterialTheme {
                        Scaffold { innerPadding ->
                            MusicDetailScreen()
                        }
                    }
                }
            }
        }
    }
}