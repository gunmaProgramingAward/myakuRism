package com.example.myaku_rismu.feature.healthDetail.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.myaku_rismu.ui.theme.customTheme
import kotlin.math.roundToInt

@Composable
fun PeriodTabList(
    periods: List<String>,
    selectedPeriod: Int,
    onClickPeriod: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .offset(y = 4.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp),
                    clip = false
                )
        )
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(
                    color = MaterialTheme.customTheme.healthDetailSelectedPeriodTabColor,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            val tabWidth = maxWidth / periods.size
            val indicatorOffset by animateDpAsState(targetValue = tabWidth * selectedPeriod)

            Box(
                modifier = Modifier
                    .offset { IntOffset(indicatorOffset.toPx().roundToInt(), 0) }
                    .width(tabWidth)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.customTheme.healthDetailPeriodTabColor,
                        shape = RoundedCornerShape(6.dp)
                    )
            )
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                periods.forEachIndexed { index, period ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null,
                                onClick = { onClickPeriod(index) }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = period,
                            color = if (selectedPeriod == index) Color.Black else Color.Gray,
                        )
                    }
                }
            }
        }
    }
}
