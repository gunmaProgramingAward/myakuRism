package com.example.myaku_rismu.core.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle

@Composable
fun AnimatedText(
    text: String,
    style: TextStyle
) {
    AnimatedContent(
        targetState = text,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        }
    ) { animatedText ->
        Text(
            text = animatedText,
            style = style
        )
    }
}