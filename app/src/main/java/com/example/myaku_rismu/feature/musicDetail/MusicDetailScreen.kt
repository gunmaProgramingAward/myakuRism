package com.example.myaku_rismu.feature.musicDetail

import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myaku_rismu.R
import com.example.myaku_rismu.ui.theme.Typography
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp

@Composable
fun MusicPlayerScreen(
    viewModel: MusicDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    fun eventHandler(event: MusicDetailUiEvent) {
        when (event) {
            is MusicDetailUiEvent.ChangeIsPlaying -> {
                viewModel.changeIsPlaying(event.boolean)
            }
            is MusicDetailUiEvent.ChangeIsFavorite -> {
                viewModel.changeIsFavorite(event.boolean)
            }
        }
    }

    var sliderPosition by remember { mutableStateOf(0.3f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

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

            TrackInfo(modifier = Modifier.padding(32.dp))

            PlayerControls(
                isPlaying = uiState.isPlaying,
                onPlayPauseClick = { eventHandler(MusicDetailUiEvent.ChangeIsPlaying(!uiState.isPlaying)) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            CustomSlider(
                value = sliderPosition,
                onValueChange = { newPosition -> sliderPosition = newPosition },
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        ActionButtons(
            isFavorite = uiState.isFavorite,
            onFavoriteClick = { eventHandler(MusicDetailUiEvent.ChangeIsFavorite(!uiState.isFavorite)) }
        )
    }
}

@Composable
fun TrackInfo(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = "Title by AI1",//TODO
            style = Typography.headlineSmall,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "2025.06.04",//TODO
            style = Typography.bodyMedium,
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
            Icon(
                Icons.Default.SkipPrevious,
                contentDescription = stringResource(id = R.string.music_detail_previous_track),
                modifier = Modifier.size(40.dp)
            )
        }
        IconButton(onClick = onPlayPauseClick) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = stringResource(if (isPlaying) R.string.music_detail_pause else R.string.music_detail_play),
                modifier = Modifier.size(64.dp)
            )
        }
        IconButton(onClick = {}) {
            Icon(
                Icons.Default.SkipNext,
                contentDescription = stringResource(id = R.string.music_detail_next_track),
                modifier = Modifier.size(40.dp)
            )
        }
    }
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

@Composable
fun CustomSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    trackColor: Color = Color.LightGray,
    progressColor: Color = Color.Black,
    thumbColor: Color = Color.Black,
    trackHeight: Dp = 6.dp,
    thumbRadius: Dp = 10.dp
) {
    val density = LocalDensity.current
    var sliderWidthPx by remember { mutableStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thumbRadius * 2 + trackHeight)
            .onSizeChanged {
                sliderWidthPx = it.width.toFloat()
            }
            .pointerInput(sliderWidthPx) {
                if (sliderWidthPx == 0f) return@pointerInput

                detectDragGestures(
                    onDragStart = { startOffset ->
                        val newValue = (startOffset.x / sliderWidthPx).coerceIn(0f, 1f)
                        onValueChange(newValue)
                    },
                    onDrag = { change, _ ->
                        val newPosition = change.position.x
                        val newValue = (newPosition / sliderWidthPx).coerceIn(0f, 1f)
                        onValueChange(newValue)
                        change.consume()
                    }
                )
            }
            .pointerInput(sliderWidthPx) {
                if (sliderWidthPx == 0f) return@pointerInput
                detectTapGestures { offset ->
                    val newValue = (offset.x / sliderWidthPx).coerceIn(0f, 1f)
                    onValueChange(newValue)
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        if (sliderWidthPx > 0f) {
            val trackHeightPx = with(density) { trackHeight.toPx() }
            val thumbRadiusPx = with(density) { thumbRadius.toPx() }

            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(
                    color = trackColor,
                    start = Offset(thumbRadiusPx, center.y),
                    end = Offset(sliderWidthPx - thumbRadiusPx, center.y),
                    strokeWidth = trackHeightPx,
                    cap = StrokeCap.Round
                )
            }

            val progressEndX = (sliderWidthPx - thumbRadiusPx * 2) * value + thumbRadiusPx
            Canvas(modifier = Modifier.fillMaxSize()) {
                if (progressEndX > thumbRadiusPx) {
                    drawLine(
                        color = progressColor,
                        start = Offset(thumbRadiusPx, center.y),
                        end = Offset(progressEndX, center.y),
                        strokeWidth = trackHeightPx,
                        cap = StrokeCap.Round
                    )
                }
            }

            val thumbCenterX = (sliderWidthPx - thumbRadiusPx * 2) * value + thumbRadiusPx
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = thumbColor,
                    radius = thumbRadiusPx,
                    center = Offset(thumbCenterX, center.y)
                )
            }
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
