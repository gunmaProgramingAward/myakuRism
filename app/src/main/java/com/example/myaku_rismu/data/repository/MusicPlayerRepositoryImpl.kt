package com.example.myaku_rismu.data.repository

import android.content.Context
import android.content.Intent
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.myaku_rismu.data.service.MusicPlaybackService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import com.example.myaku_rismu.domain.model.MusicPlayerError
import com.example.myaku_rismu.domain.model.MusicPlayerState
import com.example.myaku_rismu.domain.model.PlaybackState
import com.example.myaku_rismu.domain.model.SunoTrackDataAppModel
import com.example.myaku_rismu.domain.repository.MusicPlayerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MusicPlayerRepositoryImpl @Inject constructor(
    private val exoPlayer: ExoPlayer,
    @ApplicationContext private val context: Context
) : MusicPlayerRepository {
    private val _playerState = MutableStateFlow(MusicPlayerState())
    private var currentTrack: SunoTrackDataAppModel? = null
    private var positionUpdateJob: Job? = null

    private val mediaPlayerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            updateState {
                it.copy(
                    playbackState = if (isPlaying) PlaybackState.PLAYING else PlaybackState.PAUSED,
                    isPlaying = isPlaying
                )
            }
            if (isPlaying) {
                startPositionUpdates()
            } else {
                stopPositionUpdates()
            }
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            val newPlaybackState = when (playbackState) {
                Player.STATE_IDLE -> PlaybackState.IDLE
                Player.STATE_BUFFERING -> PlaybackState.BUFFERING
                Player.STATE_READY -> PlaybackState.READY
                Player.STATE_ENDED -> PlaybackState.ENDED
                else -> PlaybackState.ERROR
            }

            updateState { currentState ->
                val newDuration = if (newPlaybackState == PlaybackState.READY) {
                    currentTrack?.duration?.let { (it * 1000).toLong() }
                        ?: try { exoPlayer.duration } catch (e: Exception) { currentState.duration }
                } else {
                    currentState.duration
                }

                currentState.copy(
                    playbackState = newPlaybackState,
                    isPlaying = try { exoPlayer.isPlaying } catch (e: Exception) { false },
                    duration = newDuration,
                    currentPosition = if (newPlaybackState == PlaybackState.ENDED) 0L
                    else currentState.currentPosition,
                    error = null
                )
            }
        }

        override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
            stopPositionUpdates()
            val playerError = MusicPlayerError.UnknownError
            updateState {
                it.copy(
                    error = playerError,
                    playbackState = PlaybackState.ERROR,
                    isPlaying = false
                )
            }
        }

        override fun onPositionDiscontinuity(
            oldPosition: Player.PositionInfo,
            newPosition: Player.PositionInfo,
            reason: Int
        ) {
            updateState {
                it.copy(currentPosition = newPosition.positionMs)
            }
        }
    }

    init {
        exoPlayer.addListener(mediaPlayerListener)
    }

    override fun observePlayerState(): Flow<Result<MusicPlayerState>> {
        return _playerState.asStateFlow()
            .map { state ->
                if (state.error != null) {
                    Result.failure(state.error)
                } else {
                    Result.success(state.copy(error = null))
                }
            }
    }

    override suspend fun loadTrack(track: SunoTrackDataAppModel): Result<Unit>
    = withContext(Dispatchers.Main){
        try {
            currentTrack = track
            val mediaItem = MediaItem.Builder()
                .setUri(track.audioUrl)
                .setMediaMetadata(
                    androidx.media3.common.MediaMetadata.Builder()
                        .setTitle(track.title)
                        .setArtist(track.id)
                        .build()
                )
                .build()
            
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()

            updateState {
                it.copy(
                    currentTrack = track,
                    error = null
                )
            }

            Result.success(Unit)
        } catch (e: Exception) {
            val error = MusicPlayerError.UnknownError
            updateState { it.copy(error = error) }
            Result.failure(error)
        }
    }

    override suspend fun play(): Result<Unit>  = withContext(Dispatchers.Main){
        try {
//            val serviceIntent = Intent(context, MusicPlaybackService::class.java)
//            context.startForegroundService(serviceIntent)
            
            if (exoPlayer.playbackState == Player.STATE_ENDED) {
                exoPlayer.seekTo(0)
            }
            
            exoPlayer.play()
            Result.success(Unit)
        } catch (e: Exception) {
            val error = MusicPlayerError.UnknownError
            updateState { it.copy(error = error) }
            Result.failure(error)
        }
    }

    override suspend fun pause(): Result<Unit> = withContext(Dispatchers.Main) {
        try {
            exoPlayer.pause()
            Result.success(Unit)
        } catch (e: Exception) {
            val error = MusicPlayerError.UnknownError
            updateState { it.copy(error = error) }
            Result.failure(error)
        }
    }

    override suspend fun stop(): Result<Unit> = withContext(Dispatchers.Main) {
        try {
            exoPlayer.stop()
            stopPositionUpdates()
            updateState {
                it.copy(
                    playbackState = PlaybackState.IDLE,
                    currentPosition = 0L,
                    isPlaying = false
                )
            }
            Result.success(Unit)
        } catch (e: Exception) {
            val error = MusicPlayerError.UnknownError
            updateState { it.copy(error = error) }
            Result.failure(error)
        }
    }

    override suspend fun seekTo(position: Long): Result<Unit> = withContext(Dispatchers.Main) {
        try {
            exoPlayer.seekTo(position)
            updateState { it.copy(currentPosition = position) }
            Result.success(Unit)
        } catch (e: Exception) {
            val error = MusicPlayerError.UnknownError
            updateState { it.copy(error = error) }
            Result.failure(error)
        }
    }

    override suspend fun setRepeatMode(enabled: Boolean): Result<Unit> = withContext(Dispatchers.Main){
        try {
            val repeatMode = if (enabled) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
            exoPlayer.repeatMode = repeatMode
            updateState { it.copy(isRepeatMode = enabled) }
            Result.success(Unit)
        } catch (e: Exception) {
            val error = MusicPlayerError.UnknownError
            updateState { it.copy(error = error) }
            Result.failure(error)
        }
    }

    override fun getCurrentPosition(): Long {
        return try {
            exoPlayer.currentPosition
        } catch (e: IllegalStateException) {
            0L
        }
    }

    private fun startPositionUpdates() {
        stopPositionUpdates()
        positionUpdateJob = CoroutineScope(Dispatchers.Main).launch {
            while (exoPlayer.isPlaying) {
                val currentPosition = exoPlayer.currentPosition
                updateState { it.copy(currentPosition = currentPosition) }
                delay(100)
            }
        }
    }

    private fun stopPositionUpdates() {
        positionUpdateJob?.cancel()
        positionUpdateJob = null
    }

    private fun updateState(update: (MusicPlayerState) -> MusicPlayerState) {
        _playerState.value = update(_playerState.value)
    }
}