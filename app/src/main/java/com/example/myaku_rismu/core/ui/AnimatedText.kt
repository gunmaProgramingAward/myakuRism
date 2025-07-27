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

@Composable
fun AnimatedText(
    titles: List<String>,
    intervalMillis: Long = 10000L,
) {
    var index by remember { mutableIntStateOf(0) }

    LaunchedEffect(titles) {
        while (true) {
            kotlinx.coroutines.delay(intervalMillis)
            index = (index + 1) % titles.size
        }
    }

    AnimatedContent(
        targetState = index,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        }
    ) { targetIndex ->
        Text(
            text = titles[targetIndex],
            style = MaterialTheme.typography.titleSmall,
        )
    }
}