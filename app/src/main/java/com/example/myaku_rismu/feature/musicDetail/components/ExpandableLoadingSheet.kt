package com.example.myaku_rismu.feature.musicDetail.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.ui.AnimatedText
import com.example.myaku_rismu.core.ui.SkeletonBarChart
import com.example.myaku_rismu.core.ui.TopBar
import com.example.myaku_rismu.feature.home.components.GifImageLoader
import com.example.myaku_rismu.feature.home.components.LoopingRipple
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme
import com.example.myaku_rismu.ui.theme.Typography
import com.example.myaku_rismu.ui.theme.customTheme

@Composable
fun ExpandableLoadingSheet(
    modifier: Modifier = Modifier,
    onExpand: () -> Unit,
    progress: Float = 0f,
    animatedText: String,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "",
                navigationIcon = {
                    IconButton(
                        onClick = { onExpand() },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.top_bar_back_icon),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
                .background(MaterialTheme.customTheme.musicDetailMiniMusicPlayerBackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            Box(
                modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.Center,
            ) {
                val rippleColor = MaterialTheme.customTheme.homeHighBpmRippleColor

                LoopingRipple(
                    modifier = Modifier.size(350.dp),
                    beatIntervalMs = 855f,
                    newRippleStartIntervalMs = 3430,
                    bpmPlayerRippleColor = rippleColor,
                )
                Canvas(
                    modifier = Modifier.size(150.dp)
                ) {
                    drawCircle(
                        color = rippleColor,
                        radius = size.minDimension / 2,
                        center = Offset(size.width / 2, size.height / 2)
                    )
                }
                GifImageLoader(
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center),
                    gitResId = R.drawable.myaku_white
                )
            }
            Text(
                text = stringResource(R.string.ai_create_music_text),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.customTheme.loadingTextColor
            )
            Spacer(modifier = Modifier.height(7.dp))
            AnimatedText(
                text = animatedText,
                style = Typography.bodyMedium
            )
            Spacer(modifier = Modifier.size(52.dp))
            SkeletonBarChart(
                modifier = Modifier
                    .height(12.dp)
                    .padding(horizontal = 27.dp),
                progress = progress,
                progressColor = MaterialTheme.customTheme.healthDetailHeartRateThemeColor,
                barColorFaded = MaterialTheme.customTheme.myakuRismuCardColor,
            )
        }
    }
}




@Preview(showBackground = true)
@Composable
fun ExpandableLoadingSheetPreview() {
    Myaku_rismuTheme {
        ExpandableLoadingSheet(
            animatedText = "Loading...",
            onExpand = {}
        )
    }
}