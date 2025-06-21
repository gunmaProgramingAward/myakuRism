package com.example.myaku_rismu.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TitleAndSubComponent(
    modifier: Modifier = Modifier,
    title: String,
    subComponent: @Composable () -> Unit,
    titleTextStyle: TextStyle,
    spacerHeight: Dp = 16.dp,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = titleTextStyle
        )
        Spacer(Modifier.height(spacerHeight))
        subComponent()
    }
}


@Preview(showBackground = true)
@Composable
fun TitleAndSubComponentPreview() {
    TitleAndSubComponent(
        title = "Title",
        subComponent = {
            Text(text = "This is a sub-component")
        },
        titleTextStyle = TextStyle(
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.padding(16.dp)
    )
}