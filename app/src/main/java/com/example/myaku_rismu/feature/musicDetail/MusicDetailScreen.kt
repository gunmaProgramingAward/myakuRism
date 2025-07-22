package com.example.myaku_rismu.feature.musicDetail

import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myaku_rismu.R
import com.example.myaku_rismu.ui.theme.Typography
import com.example.myaku_rismu.ui.theme.Myaku_rismuTheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

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

            Spacer(modifier = Modifier.height(32.dp))
            TrackInfo()
            Spacer(modifier = Modifier.height(32.dp))

            PlayerControls(
                isPlaying = uiState.isPlaying,
                onPlayPauseClick = { eventHandler(MusicDetailUiEvent.ChangeIsPlaying(!uiState.isPlaying)) }
            )

            Spacer(modifier = Modifier.height(16.dp))

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
fun TrackInfo() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
    modifier: Modifier = Modifier
) {
    val thumbPainter = painterResource(id = R.drawable.slider_thumb)
    val trackPainter = painterResource(id = R.drawable.slider_track)
    val progressPainter = painterResource(id = R.drawable.slider_progress)

    var sliderWidth by remember { mutableStateOf(0) }
    val density = LocalDensity.current.density
    val thumbSize = 24.dp //下で使ってるのに、、、

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .onSizeChanged { newSize ->
                sliderWidth = newSize.width
            }
            .pointerInput(sliderWidth) {
                if (sliderWidth == 0) return@pointerInput

                detectDragGestures { change, _ ->
                    val newPosition = change.position.x
                    val newValue = (newPosition / sliderWidth).coerceIn(0f, 1f)
                    onValueChange(newValue)
                    change.consume()
                }
            },
        contentAlignment = Alignment.CenterStart
    ) {
        val thumbSize = 24.dp
        val trackHeight = 8.dp

        if (sliderWidth > 0) {
            val thumbSizePx = thumbSize.value * density
            val thumbOffset = (sliderWidth * value) - (thumbSizePx / 2)

            Image(
                painter = trackPainter,
                contentDescription = stringResource(id = R.string.music_detail_track),
                modifier = Modifier.fillMaxWidth().height(trackHeight),
                contentScale = ContentScale.FillBounds //これを使うと線が太くなりますが、使わないと線の太さが薄くなったり太くなったりします。
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(value)
                    .height(trackHeight)
                    .clip(RoundedCornerShape(topStart = 100f, bottomStart = 100f))
            ) {
                Image(
                    painter = progressPainter,
                    contentDescription = stringResource(id = R.string.music_detail_progress),
                    modifier = Modifier.fillMaxWidth().height(trackHeight),
                    contentScale = ContentScale.FillBounds,//これを使うと線が太くなりますが、使わないと線の太さが薄くなったり太くなったりします。
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }

            Image(
                painter = thumbPainter,
                contentDescription = stringResource(id = R.string.music_detail_thumb),
                modifier = Modifier
                    .offset(x = (thumbOffset / LocalDensity.current.density).dp)
                    .size(thumbSize),
                contentScale = ContentScale.FillBounds

            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MusicPlayerScreenPreview() {
    val fakeViewModel: MusicDetailViewModel = viewModel()
    Myaku_rismuTheme {
        MusicPlayerScreen(viewModel = fakeViewModel)
    }
}