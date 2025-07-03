package com.example.myaku_rismu.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myaku_rismu.R
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme
import com.example.myaku_rismu.ui.theme.customTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {

    var bpmLevel by remember { mutableIntStateOf(0) }
    var bpmCount by remember { mutableIntStateOf(0) }

    val bpmColor = when (bpmLevel) {
        1 -> MaterialTheme.customTheme.homeMediumBpmColor
        2 -> MaterialTheme.customTheme.homeHighBpmColor
        else -> MaterialTheme.customTheme.homeLowBpmColor
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        BpmPlayerCard(
            modifier = Modifier,
            bpmCount = bpmCount,
            bpmColor = bpmColor
        )
    }
}


@Composable
fun BpmPlayerCard(
    modifier: Modifier = Modifier,
    bpmCount: Int,
    bpmColor: androidx.compose.ui.graphics.Color
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(310.dp)
            .background(bpmColor)
    ) {
        Image(
            painter = painterResource(id = R.drawable.shape),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$bpmCount",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 40.sp
                ),
                modifier = Modifier.padding(top = 14.dp)
            )
            Text(
                text = stringResource(R.string.bpm),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 20.sp
                ),
                modifier = Modifier.offset(y = (-8).dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /*音楽生成*/ },
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .height(36.dp)
                    .fillMaxWidth(0.3f),
                contentPadding = PaddingValues(vertical = 0.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.customTheme.settingScreenCardColor),
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.play),
                    color = MaterialTheme.customTheme.settingScreenTextColor,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    Myaku_rismuTheme {
        HomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun BpmPlayerCardPreview() {
    Myaku_rismuTheme {
        BpmPlayerCard(bpmCount = 174, bpmColor = MaterialTheme.customTheme.homeMediumBpmColor)
    }
}