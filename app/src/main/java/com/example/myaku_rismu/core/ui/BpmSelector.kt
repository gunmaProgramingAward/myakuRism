package com.example.myaku_rismu.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myaku_rismu.R
import com.example.myaku_rismu.ui.theme.customTheme


@Composable
fun BpmSelector(
    modifier: Modifier = Modifier,
    selectedBPM: Int,
    onBPMSelected: (Int) -> Unit,
    bpmValues: List<Int>,
) {
    val bpmLabels = listOf(
        stringResource(R.string.bpm_min),
        stringResource(R.string.bpm_avg),
        stringResource(R.string.bpm_max)
    )
    val noDataText = stringResource(R.string.no_data)

    Column(modifier = modifier) {
        Box {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                bpmValues.forEachIndexed { index, value ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index, bpmValues.size),
                        onClick = { onBPMSelected(value) },
                        enabled = value != 0,
                        selected = if (value != 0) selectedBPM == value else false,
                        modifier = Modifier.weight(1f),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = MaterialTheme.customTheme.homeHeartRateBarColorFaded,
                            activeContentColor = Color.White,
                            inactiveContainerColor = Color.White,
                            inactiveContentColor = Color.Black,
                        )
                    ) {
                        Text(
                            text = if (value != 0) "${value}${stringResource(R.string.unit_bpm)}" else noDataText,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            if (bpmValues[1] == 0) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(MaterialTheme.customTheme.disabledBackgroundColor)
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            bpmLabels.forEach { label ->
                Text(
                    text = label,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MyScreen() {
    val bpmValues = listOf(40, 96, 160)

    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BpmSelector(
            selectedBPM = 96,
            onBPMSelected = {},
            bpmValues = bpmValues
        )
    }
}