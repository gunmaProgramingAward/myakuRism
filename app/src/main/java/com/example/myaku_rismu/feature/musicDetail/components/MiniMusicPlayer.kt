package com.example.myaku_rismu.feature.musicDetail.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.myaku_rismu.R
import com.example.myaku_rismu.feature.home.components.BarChart
import com.example.myaku_rismu.ui.theme.Typography
import com.example.myaku_rismu.ui.theme.customTheme

@Composable
fun MiniMusicPlayer(
    isPlaying: Boolean,
    onPlayPauseClick: () -> Unit,
    onExpand: () -> Unit,
    modifier: Modifier = Modifier,
    title: String,
    type: String,
    image: String,
    isCreating: Boolean,
    progress: Float = 0.6f
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
            .clickable { onExpand() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.customTheme.musicDetailMiniMusicPlayerBackgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isCreating) {
                // ----- テスト用 ---
                var progress by remember { mutableStateOf(0f) }
                
                LaunchedEffect(Unit) {
                    while (progress < 1f) {
                        progress += 0.01f
                        kotlinx.coroutines.delay(30)
                    }
                }
                // ----------------
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    BarChart(
                        modifier = Modifier.size(8.dp),
                        progress = progress,
                        progressColor = MaterialTheme.customTheme.healthDetailHeartRateThemeColor,
                        barColorFaded = MaterialTheme.customTheme.myakuRismuCardColor,
                    )
                }
            } else {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = Typography.bodyMedium.copy(fontSize = 14.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = type,
                        style = Typography.bodySmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = onPlayPauseClick,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) stringResource(R.string.music_detail_pause)
                        else stringResource(R.string.music_detail_play),
                        tint = MaterialTheme.customTheme.appTextColor,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MiniMusicPlayerPreview() {
    MiniMusicPlayer(
        isPlaying = true,
        onPlayPauseClick = {},
        onExpand = {},
        modifier = Modifier.fillMaxWidth(),
        title = "Sample Song",
        type = "Sample Artist",
        image = "https://placehold.jp/3d4070/ffffff/150x150.png",
        isCreating = true
    )
}