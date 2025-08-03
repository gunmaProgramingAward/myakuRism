package com.example.myaku_rismu.feature.home.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import com.example.myaku_rismu.feature.home.WaveState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun LoopingRipple(
    modifier: Modifier = Modifier,
    beatIntervalMs: Float,
    newRippleStartIntervalMs: Int,
    bpmPlayerRippleColor: Color,
    initialDelayMs: Long = 534L,
    initialScale: Float = 0f,
) {
    val rippleCount = 4  // 同時に表示する波紋の数

    val waves = remember {
        List(rippleCount) {
            WaveState(
                scale = Animatable(0f),
                alpha = Animatable(0f)
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(initialDelayMs) // 初期遅延を設定（波紋の初期位置を制御）
        var waveIndex = 0

        while (true) {
            val currentWave = waves[waveIndex % rippleCount]

            launch {
                currentWave.scale.snapTo(initialScale)
                currentWave.alpha.snapTo(1f)

                launch {
                    currentWave.scale.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = newRippleStartIntervalMs,
                            easing = LinearEasing
                        )
                    )
                }

                launch {
                    currentWave.alpha.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(
                            durationMillis = newRippleStartIntervalMs - 200,
                            delayMillis = 200,
                            easing = LinearEasing
                        )
                    )
                }
            }
            waveIndex++
            delay(beatIntervalMs.toLong())
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.matchParentSize()
        ) {
            val maxRippleSpreadRadiusPx = this.size.minDimension / 2

            waves.forEach { wave ->
                val currentScale = wave.scale.value
                val currentAlpha = wave.alpha.value

                if (currentAlpha > 0f) {
                    drawCircle(
                        color = bpmPlayerRippleColor.copy(alpha = currentAlpha),
                        radius = maxRippleSpreadRadiusPx * currentScale,
                        style = Fill
                    )
                }
            }
        }
    }
}
