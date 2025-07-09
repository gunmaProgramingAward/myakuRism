package com.example.myaku_rismu.feature.musicDetail

import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myaku_rismu.R
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme

@Composable
fun MusicPlayerScreen() {
    var isPlaying by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    var sliderPosition by remember { mutableStateOf(0.3f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopAppBar()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.height(32.dp))
            TrackInfo()
            Spacer(modifier = Modifier.height(32.dp))

            PlayerControls(
                isPlaying = isPlaying,
                onPlayPauseClick = { isPlaying = !isPlaying }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PlaybackSlider(
                value = sliderPosition,
                onValueChange = { newPosition -> sliderPosition = newPosition }
            )
        }

        ActionButtons(
            isFavorite = isFavorite,
            onFavoriteClick = { isFavorite = !isFavorite }
        )
    }
}

@Composable
fun TopAppBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {}) {
            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "닫기")
        }
        IconButton(onClick = {}) {
            Icon(Icons.Default.Info, contentDescription = "정보")
        }
    }
}

@Composable
fun TrackInfo() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Title by AI1",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "2025.06.04",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun PlayerControls(isPlaying: Boolean, onPlayPauseClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = {}) {
            Icon(Icons.Default.SkipPrevious, contentDescription = stringResource(id = R.string.music_detail_previous_track), modifier = Modifier.size(40.dp))
        }
        IconButton(onClick = onPlayPauseClick) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = stringResource(if (isPlaying) R.string.music_detail_pause else R.string.music_detail_play),
                modifier = Modifier.size(64.dp)
            )
        }
        IconButton(onClick = {}) {
            Icon(Icons.Default.SkipNext, contentDescription = stringResource(id = R.string.music_detail_next_track), modifier = Modifier.size(40.dp))
        }
    }
}

@Composable
fun PlaybackSlider(value: Float, onValueChange: (Float) -> Unit) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.primary,
            activeTrackColor = MaterialTheme.colorScheme.primary,
            inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}

@Composable
fun ActionButtons(isFavorite: Boolean, onFavoriteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = stringResource(id = R.string.music_detail_favorite),
                tint = if (isFavorite) Color.Red else LocalContentColor.current
            )
        }
        IconButton(onClick = {}) {
            Icon(Icons.Default.Shuffle, contentDescription = stringResource(id = R.string.music_detail_suffle))
        }
        IconButton(onClick = {}) {
            Icon(Icons.Default.Share, contentDescription = stringResource(id = R.string.music_detail_share))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MusicPlayerScreenPreview() {
    Myaku_rismuTheme {
        MusicPlayerScreen()
    }
}