package com.example.myaku_rismu.feature.musicDetail

import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myaku_rismu.feature.musicDetail.components.ExpandedMusicPlayer
import com.example.myaku_rismu.feature.musicDetail.components.MiniMusicPlayer
import com.example.myaku_rismu.ui.theme.customTheme
import kotlinx.coroutines.delay

// TODO: 仮に値
const val subTitle = "HIPHOP"

@Composable
fun MusicDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: MusicDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val playerState by viewModel.playerState.collectAsState()

    val rotationAnimatable = remember { Animatable(uiState.musicImagePausedRotation) }
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val screenHeight = configuration.screenHeightDp.dp
    val dragThreshold = with(density) { 100.dp.toPx() }

    val animationProgress by animateFloatAsState(
        targetValue = if (uiState.playerState == PlayerState.EXPANDED) 1f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            easing = FastOutSlowInEasing
        ),
    )

    fun eventHandler(event: MusicDetailUiEvent) {
        when (event) {
            is MusicDetailUiEvent.TogglePlayPause -> {
                viewModel.togglePlayPause()
            }

            is MusicDetailUiEvent.SeekToPosition -> {
                viewModel.seekToPosition(event.position)
            }

            is MusicDetailUiEvent.ToggleRepeatMode -> {
                viewModel.toggleRepeatMode()
            }

            is MusicDetailUiEvent.ChangePlayerState -> {
                viewModel.changePlayerState(event.playerState)
            }
            is MusicDetailUiEvent.ChangeFavoriteState -> {
                viewModel.changeIsFavorite(event.isFavorite)
            }

            is MusicDetailUiEvent.ChangeMusicImagePausedRotation -> {
                viewModel.changeMusicImagePausedRotation(event.rotation)
            }

            is MusicDetailUiEvent.ChangeDragOffset -> {
                viewModel.changeDragOffset(event.offset)
            }

            is MusicDetailUiEvent.PlusDragOffset -> {
                viewModel.plusDragOffset(event.offset)
            }
        }
    }

    LaunchedEffect(playerState.isPlaying) {
        if (playerState.isPlaying) {
            while (true) {
                val currentValue = rotationAnimatable.value
                val nextTarget = currentValue + 360f
                rotationAnimatable.animateTo(
                    targetValue = nextTarget,
                    animationSpec = tween(durationMillis = 15000, easing = LinearEasing)
                )
            }
        } else {
            rotationAnimatable.stop()
            val normalizedRotation = rotationAnimatable.value % 360f
            rotationAnimatable.snapTo(normalizedRotation)
            eventHandler(MusicDetailUiEvent.ChangeMusicImagePausedRotation(normalizedRotation))
        }
    }

    LaunchedEffect(uiState.playerState) {
        if (uiState.playerState == PlayerState.COLLAPSED) {
            delay(300)
            eventHandler(MusicDetailUiEvent.ChangeDragOffset(0f))
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(uiState.playerState) {
                if (uiState.playerState == PlayerState.EXPANDED) {
                    detectDragGestures(
                        onDragEnd = {
                            if (uiState.dragOffset > dragThreshold) {
                                eventHandler(MusicDetailUiEvent.ChangePlayerState(PlayerState.COLLAPSED))
                            } else {
                                eventHandler(MusicDetailUiEvent.ChangeDragOffset(0f))
                            }
                        }
                    ) { _, dragAmount ->
                        eventHandler(MusicDetailUiEvent.PlusDragOffset(dragAmount.y))
                    }
                }
            }
            .then(
                if (animationProgress < 0.5) {
                    Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                } else {
                    Modifier.padding(horizontal = 0.dp, vertical = 0.dp)
                }
            )
    ) {
        val playerHeight = (64.dp + (screenHeight - 64.dp) * animationProgress)
        val cornerRadius = (12.dp * (1f - animationProgress))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(playerHeight)
                .zIndex(if (uiState.playerState == PlayerState.EXPANDED) 10f else 1f)
                .graphicsLayer {
                    translationY = uiState.dragOffset.coerceAtLeast(0f)
                },
            shape = RoundedCornerShape(
                topStart = cornerRadius,
                topEnd = cornerRadius,
                bottomEnd = if (animationProgress > 0.2f) 0.dp else cornerRadius,
                bottomStart = if (animationProgress > 0.2f) 0.dp else cornerRadius
            ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.customTheme.musicDetailMiniMusicPlayerBackgroundColor
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            if (animationProgress < 0.2f) {
                MiniMusicPlayer(
                    title = playerState.currentTrack?.title,
                    type = subTitle,
                    isPlaying = playerState.isPlaying,
                    togglePlayPause = {
                        eventHandler(MusicDetailUiEvent.TogglePlayPause)
                    },
                    onExpand = {
                        eventHandler(MusicDetailUiEvent.ChangePlayerState(PlayerState.EXPANDED))
                    },
                    modifier = Modifier
                        .fillMaxSize(),
                    image = playerState.currentTrack?.imageUrl,
                )
            } else {
                ExpandedMusicPlayer(
                    uiState = uiState,
                    playerState = playerState,
                    eventHandler = { eventHandler(it) },
                    sliderPosition = if (playerState.duration > 0) {
                        playerState.currentPosition.toFloat() / playerState.duration.toFloat()
                    } else 0f,
                    title = playerState.currentTrack?.title,
                    subTitle = subTitle,
                    image = playerState.currentTrack?.imageUrl,
                    rotation = rotationAnimatable.value,
                )
            }
        }
    }
}

@Preview
@Composable
fun MusicDetailScreenPreview() {
    MusicDetailScreen(
        modifier = Modifier.fillMaxSize(),
        viewModel = hiltViewModel()
    )
}