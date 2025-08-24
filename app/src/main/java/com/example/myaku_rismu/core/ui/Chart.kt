package com.example.myaku_rismu.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun BarChart(
    modifier: Modifier = Modifier,
    progress: Float,
    progressColor: Color,
    barColorFaded: Color,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(barColorFaded, RoundedCornerShape(5.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (progress > 0f) {
            Box(
                modifier = Modifier
                    .weight(progress.coerceIn(0f, 1f))
                    .fillMaxHeight()
                    .background(progressColor, RoundedCornerShape(5.dp))
            )
        }
        if (progress < 1f) {
            Box(
                modifier = Modifier
                    .weight((1f - progress).coerceIn(0f, 1f))
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
fun SkeletonBarChart(
    modifier: Modifier = Modifier,
    progress: Float,
    progressColor: Color,
    barColorFaded: Color
) {
    val infiniteTransition = rememberInfiniteTransition(label = "skeleton_animation")
    val shimmerPosition by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_animation"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(barColorFaded, RoundedCornerShape(5.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (progress > 0f) {
            BoxWithConstraints(
                modifier = Modifier
                    .weight(progress.coerceIn(0f, 1f))
                    .fillMaxHeight()
            ) {
                val widthPx = constraints.maxWidth.toFloat()
                val shimmerWidth = widthPx * 0.4f
                val startX = (widthPx - shimmerWidth) * shimmerPosition
                val endX = startX + shimmerWidth

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    progressColor.copy(alpha = 0.7f),
                                    progressColor.copy(alpha = 0.8f),
                                    progressColor.copy(alpha = 0.9f),
                                    progressColor.copy(alpha = 1f),
                                    progressColor.copy(alpha = 0.9f),
                                    progressColor.copy(alpha = 0.8f),
                                    progressColor.copy(alpha = 0.7f),
                                ),
                                startX = startX,
                                endX = endX
                            ),
                            shape = RoundedCornerShape(5.dp)
                        )
                )
            }
        }
        if (progress < 1f) {
            Box(
                modifier = Modifier
                    .weight((1f - progress).coerceIn(0f, 1f))
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
fun DonutChart(
    progress: Float,
    progressColor: Color,
    barColorFaded: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .height(80.dp)
                .width(80.dp)
                .padding(6.dp)
        ) {
            val sweepAngle = (progress * 360f).coerceIn(0f, 360f)
            if (sweepAngle < 360f) {
                drawArc(
                    color = barColorFaded,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(
                        width = size.width * 0.2f,
                    )
                )
            }
            if (sweepAngle > 0f) {
                drawArc(
                    color = progressColor,
                    startAngle = 270f,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    style = Stroke(
                        width = size.width * 0.15f,
                        cap = StrokeCap.Round
                    )
                )
            }
        }
    }
}