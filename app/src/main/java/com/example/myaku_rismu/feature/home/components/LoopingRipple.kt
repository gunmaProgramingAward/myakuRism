package com.example.myaku_rismu.feature.home.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.myaku_rismu.feature.home.WaveState
import com.example.myaku_rismu.ui.theme.customTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun LoopingRipple(
    modifier: Modifier = Modifier,
    beatIntervalMs: Float,
    newRippleStartIntervalMs: Int,
    bpmPlayerColor: Color
) {





    // === アニメーションのパラメータ設定 ===
    val rippleCount = 4                     // 同時に表示する波紋の数
    val animationTotalDurationMs = newRippleStartIntervalMs // 1つの波紋の総アニメーション時間（ミリ秒）
    val newRippleStartIntervalMs = beatIntervalMs.toInt() // 新しい波紋の開始間隔（ミリ秒）

    // 各波紋の状態を管理するためのリスト
    val waves = remember {
        List(rippleCount) {
            WaveState(
                scale = Animatable(0f), // 初期拡大率：0（見えない状態）
                alpha = Animatable(0f)  // 初期透明度：0（完全透明）
            )
        }
    }

    LaunchedEffect(Unit) {
        var waveIndex = 0 // 現在処理中の波紋のインデックス

        // 無限ループでアニメーションを継続
        while (true) {
            // 現在の波紋を取得（rippleCountで割った余りを使用して循環）
            val currentWave = waves[waveIndex % rippleCount]

            launch {
                // === 波紋の初期化 ===
                currentWave.scale.snapTo(0f)
                currentWave.alpha.snapTo(1f)

                launch {
                    currentWave.scale.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = animationTotalDurationMs,
                            easing = LinearEasing  // 等速アニメーション
                        )
                    )
                }

                launch {
                    currentWave.alpha.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(
                            durationMillis = animationTotalDurationMs - 200,
                            delayMillis = 200,
                            easing = LinearEasing
                        )
                    )
                }
            }
            waveIndex++
            delay(newRippleStartIntervalMs.toLong())
        }
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(208.dp)
        ) {
            val characterVisualRadiusPx = 0.dp.toPx()
            // 波紋が拡大できる最大半径（キャンバスの半分のサイズ）
            val maxRippleSpreadRadiusPx = this.size.minDimension / 2

            waves.forEach { wave ->
                val currentScale = wave.scale.value
                val currentAlpha = wave.alpha.value

                if (currentAlpha > 0f) {
                    // 現在の拡大率に基づいて波紋の半径を計算
                    val animatedRadius = characterVisualRadiusPx +
                            (maxRippleSpreadRadiusPx - characterVisualRadiusPx) * currentScale
                    drawCircle(
                        color = bpmPlayerColor.copy(alpha = currentAlpha),
                        radius = animatedRadius,
                        style = Stroke(width = 40.dp.toPx()),
                    )
                }
            }
        }

    }
}
