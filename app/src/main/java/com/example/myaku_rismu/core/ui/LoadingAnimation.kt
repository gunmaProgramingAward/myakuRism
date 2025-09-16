package com.example.myaku_rismu.core.ui

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myaku_rismu.R
import com.example.myaku_rismu.ui.theme.customTheme
import kotlin.math.exp
import kotlin.math.pow

@Composable
fun LoadingAnimation(
    modifier: Modifier = Modifier,
    loadingText: String = stringResource(R.string.Loading_text),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.customTheme.loadingBackgroundColor.copy(alpha = 0.85f)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GifImageLoader(
            modifier = Modifier
                .size(180.dp),
            gitResId = R.drawable.my_gif,
            color = MaterialTheme.customTheme.loadingIconColor
        )
        JumpyRow(
            modifier = Modifier
                .offset(y = (-52).dp)
        ) {
            for (s in loadingText) {
                Text(
                    text = s.toString(),
                    color = MaterialTheme.customTheme.loadingBottomTextColor,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp
                )
            }
        }
    }
}

@Composable
fun JumpyRow(
    modifier: Modifier = Modifier,
    waveWidth: Dp = 200.dp,
    waveHeight: Dp = 24.dp,
    animationSpec: InfiniteRepeatableSpec<Float> = infiniteRepeatable(
        animation = tween(3000, easing = LinearEasing),
        repeatMode = RepeatMode.Reverse
    ),
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition("Wave Transition")
    val waveProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = animationSpec,
        label = "Wave Progress"
    )

    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val waveWidthPx = waveWidth.roundToPx()
        val waveHeightPx = waveHeight.roundToPx()

        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        val rowWidth = placeables.sumOf { it.width }
        val maxHeight = placeables.maxOf { it.height }
        val rowHeight = maxHeight + waveHeightPx

        layout(width = rowWidth, height = rowHeight) {
            var xPosition = 0

            val totalDistance = rowWidth + waveWidthPx
            val waveStart = -waveWidthPx + (totalDistance * waveProgress)
            val waveEnd = waveStart + waveWidthPx

            placeables.forEach { placeable ->
                val itemCenterX = xPosition + (placeable.width / 2f)
                val baseYPosition = rowHeight - placeable.height

                val yPosition = if (itemCenterX in waveStart..waveEnd) {
                    val normalizedX = normalizeX(itemCenterX, waveStart, waveEnd, -2f, 2f)
                    val waveEffect = waveCurve(normalizedX)
                    (baseYPosition - waveHeightPx * waveEffect).toInt()
                } else {
                    baseYPosition
                }

                // アイテムを配置します
                placeable.place(x = xPosition, y = yPosition)
                xPosition += placeable.width
            }
        }
    }
}

fun normalizeX(
    x: Float,
    originalMin: Float,
    originalMax: Float,
    targetMin: Float,
    targetMax: Float
): Float {
    return targetMin + ((x - originalMin) / (originalMax - originalMin)) * (targetMax - targetMin)
}

fun waveCurve(x: Float): Float {
    return exp(-x.pow(2))
}

@Composable
@Preview
fun LoadingAnimationPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LoadingAnimation()
    }
}
