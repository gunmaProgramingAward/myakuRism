package com.example.myaku_rismu.feature.home.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun BarChart(
    progress: Float,
    progressColor: Color,
    barColorFaded: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
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
fun DonutChart(
    progress: Float,
    progressColor: Color,
    barColorFaded: Color,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
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
                    startAngle = 270f + sweepAngle,
                    sweepAngle = 360f - sweepAngle,
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
                        width = size.width * 0.2f,
                        cap = StrokeCap.Round
                    )
                )
            }
        }
    }
}