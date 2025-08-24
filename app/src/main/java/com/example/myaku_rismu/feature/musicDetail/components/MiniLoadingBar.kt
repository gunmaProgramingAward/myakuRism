package com.example.myaku_rismu.feature.musicDetail.components

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myaku_rismu.core.ui.AnimatedText
import com.example.myaku_rismu.core.ui.SkeletonBarChart
import com.example.myaku_rismu.ui.theme.customTheme

@Composable
fun MiniLoadingBar(
    onExpand: () -> Unit,
    modifier: Modifier = Modifier,
    progress: Float = 0.0f,
    animatedText: String,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .clickable { onExpand() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.customTheme.musicDetailMiniMusicPlayerBackgroundColor
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            AnimatedText(
                text = animatedText,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.size(10.dp))
            SkeletonBarChart(
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
        animatedText = "Loading...",
        onExpand = {},
        modifier = Modifier.fillMaxWidth(),
    )
}