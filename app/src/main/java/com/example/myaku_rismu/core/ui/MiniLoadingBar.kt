package com.example.myaku_rismu.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myaku_rismu.R
import com.example.myaku_rismu.feature.home.components.BarChart
import com.example.myaku_rismu.ui.theme.customTheme

@Composable
fun MiniLoadingBar(
    onExpand: () -> Unit,
    modifier: Modifier = Modifier,
    progress: Float = 0.6f
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .clickable { onExpand() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.customTheme.musicDetailMiniMusicPlayerBackgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        // ----- テスト用 ---
        var progress by remember { mutableFloatStateOf(0f) }

        LaunchedEffect(Unit) {
            while (progress < 1f) {
                progress += 0.01f
                kotlinx.coroutines.delay(30)
            }
        }
        // ----------------

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            AnimatedText(
                titles = stringArrayResource(id = R.array.generation_messages).toList()
            )
            Spacer(modifier = Modifier.size(10.dp))
            BarChart(
                modifier = Modifier
                    .height(8.dp)
                    .padding(horizontal = 2.dp),
                progress = progress,
                progressColor = MaterialTheme.customTheme.healthDetailHeartRateThemeColor,
                barColorFaded = MaterialTheme.customTheme.myakuRismuCardColor,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MiniLoadingBarPreview() {
    MiniLoadingBar(
        onExpand = {},
        modifier = Modifier.fillMaxWidth(),
    )
}