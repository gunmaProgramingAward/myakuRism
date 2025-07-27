package com.example.myaku_rismu.feature.musicDetail.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.ui.TopBar
import com.example.myaku_rismu.feature.musicDetail.MusicDetailState
import com.example.myaku_rismu.feature.musicDetail.MusicDetailUiEvent
import com.example.myaku_rismu.feature.musicDetail.PlayerState
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme
import com.example.myaku_rismu.ui.theme.Typography
import com.example.myaku_rismu.ui.theme.customTheme

@Composable
fun ExpandedMusicPlayer(
    modifier: Modifier = Modifier,
    uiState: MusicDetailState,
    eventHandler: (MusicDetailUiEvent) -> Unit,
    sliderPosition: Float,
    title: String,
    subTitle: String,
    image: String,
    rotation: Float,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = "",
                navigationIcon = {
                    IconButton(
                        onClick = {
                            eventHandler(MusicDetailUiEvent.ChangePlayerState(PlayerState.COLLAPSED))
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.top_bar_back_icon),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
                .background(MaterialTheme.customTheme.musicDetailMiniMusicPlayerBackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
            AsyncImage(
                model = image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(1f)
                    .clip(CircleShape)
                    .background(Color.LightGray)
                    .graphicsLayer {
                        rotationZ = rotation
                    }
            )
            TrackInfo(
                title = title,
                subTitle = subTitle,
                modifier = Modifier
                    .padding(horizontal = 28.dp)
                    .padding(top = 40.dp, bottom = 16.dp)
            )
            CustomSlider(
                value = sliderPosition,
                onValueChange = { newPosition ->
                    eventHandler(MusicDetailUiEvent.ChangeMusicSliderPosition(newPosition))
                                },
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            PlayerControls(
                isPlaying = uiState.isPlaying,
                isFavorite = uiState.isFavorite,
                isShuffle = uiState.isShuffle,
                onPlayPauseClick = {
                    eventHandler(MusicDetailUiEvent.ChangeIsPlaying(!uiState.isPlaying))
                },
                onFavoriteClick = {
                    eventHandler(MusicDetailUiEvent.ChangeIsFavorite(!uiState.isFavorite))
                },
                onShuffleClick = {
                    eventHandler(MusicDetailUiEvent.ChangeIsShuffle(!uiState.isShuffle))
                },
                leftArrowClick = {},
                rightArrowClick = {},
                modifier = Modifier
                    .padding(top = 24.dp)
            )
        }
    }
}

@Composable
fun TrackInfo(
    modifier: Modifier = Modifier,
    title: String,
    subTitle: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = title,
            style = Typography.headlineSmall,
            color = Color.Black,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subTitle,
            style = Typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun PlayerControls(
    isPlaying: Boolean,
    isFavorite: Boolean,
    isShuffle: Boolean,
    onFavoriteClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onShuffleClick: () -> Unit,
    leftArrowClick: () -> Unit,
    rightArrowClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onFavoriteClick) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = stringResource(id = R.string.music_detail_favorite),
                tint = if (isFavorite) MaterialTheme.customTheme.musicDetailMusicPlayerFavorite
                else LocalContentColor.current,
                modifier = Modifier.size(32.dp)
            )
        }
        IconButton(
            onClick = { leftArrowClick() },
            modifier = Modifier
                .size(56.dp)
        ) {
            Icon(
                Icons.Default.SkipPrevious,
                contentDescription = stringResource(id = R.string.music_detail_previous_track),
                modifier = Modifier.size(56.dp)
            )
        }
        IconButton(
            onClick = onPlayPauseClick,
            modifier = Modifier
                .size(56.dp)
        ) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = stringResource(
                    if (isPlaying) R.string.music_detail_pause
                    else R.string.music_detail_play
                ),
                modifier = Modifier.size(56.dp)
            )
        }
        IconButton(
            onClick = { rightArrowClick() },
            modifier = Modifier
                .size(56.dp)
        ) {
            Icon(
                Icons.Default.SkipNext,
                contentDescription = stringResource(id = R.string.music_detail_next_track),
                modifier = Modifier.size(56.dp)
            )
        }
        IconButton(onClick = { onShuffleClick() }) {
            Icon(
                Icons.Default.Shuffle,
                contentDescription = stringResource(id = R.string.music_detail_suffle),
                tint = if (isShuffle) MaterialTheme.customTheme.musicDetailMusicPlayerShuffle
                else Color.Black,
                modifier = Modifier.size(32.dp)
            )
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
        ExpandedMusicPlayer(
            uiState = MusicDetailState(
                isPlaying = true,
                isFavorite = false,
                isShuffle = false,
                playerState = PlayerState.EXPANDED
            ),
            eventHandler = {},
            sliderPosition = 0.5f,
            title = "Sample Song",
            subTitle = "Sample Artist",
            image = "https://via.placeholder.com/150",
            rotation = 0f,
        )
    }
}