package com.example.myaku_rismu.data.repository

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.example.myaku_rismu.domain.model.MusicPlayerError
import com.example.myaku_rismu.domain.model.MusicPlayerState
import com.example.myaku_rismu.domain.model.PlaybackState
import com.example.myaku_rismu.domain.model.SunoTrackDataAppModel
import com.example.myaku_rismu.domain.repository.MusicPlayerRepository
import com.google.common.util.concurrent.ListenableFuture
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
    @ApplicationContext private val context: Context,
    private val mediaControllerFuture: ListenableFuture<MediaController>
) : MusicPlayerRepository {
    private val _playerState = MutableStateFlow(MusicPlayerState())
    private var currentTrack: SunoTrackDataAppModel? = null
    private var positionUpdateJob: Job? = null
    private var mediaController: MediaController? = null

    private val mediaPlayerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            updateState {
                it.copy(
                    playbackState = if (isPlaying) PlaybackState.PLAYING else PlaybackState.PAUSED,
                    isPlaying = isPlaying
                )
            }
            if (isPlaying) startPositionUpdates() else stopPositionUpdates()
        }

        override fun onPlaybackStateChanged(playbackState: Int) {
            val newPlaybackState = mapPlayerState(playbackState)
            updateState { currentState ->
                currentState.copy(
                    playbackState = newPlaybackState,
                    isPlaying = safeGetIsPlaying(),
                    duration = if (newPlaybackState == PlaybackState.READY) getDuration(currentState)
                    else currentState.duration,
                    currentPosition = if (newPlaybackState == PlaybackState.ENDED) 0L
                    else currentState.currentPosition,
                    error = null
                )
            }
        }

        override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
            stopPositionUpdates()
            updateState {
                it.copy(
                    error = MusicPlayerError.UnknownError,
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
            updateState { it.copy(currentPosition = newPosition.positionMs) }
        }
    }

    init {
        setupMediaController()
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

    override suspend fun loadTrack(track: SunoTrackDataAppModel): Result<Unit> = 
        withContext(Dispatchers.Main) {
            try {
                currentTrack = track
                val mediaItem = createMediaItem(track)
                
                mediaController?.apply {
                    setMediaItem(mediaItem)
                    prepare()
                }
                
                updateState { it.copy(currentTrack = track, error = null) }
                Result.success(Unit)
            } catch (e: Exception) {
                handleError(MusicPlayerError.UnknownError)
            }
        }

    override suspend fun play(): Result<Unit> = withContext(Dispatchers.Main) {
        try {
            mediaController?.apply {
                if (playbackState == Player.STATE_ENDED) seekTo(0)
                play()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            handleError(MusicPlayerError.UnknownError)
        }
    }

    override suspend fun pause(): Result<Unit> = withContext(Dispatchers.Main) {
        try {
            mediaController?.pause()
            Result.success(Unit)
        } catch (e: Exception) {
            handleError(MusicPlayerError.UnknownError)
        }
    }

    override suspend fun stop(): Result<Unit> = withContext(Dispatchers.Main) {
        try {
            mediaController?.stop()
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
            handleError(MusicPlayerError.UnknownError)
        }
    }

    override suspend fun seekTo(position: Long): Result<Unit> = withContext(Dispatchers.Main) {
        try {
            mediaController?.seekTo(position)
            updateState { it.copy(currentPosition = position) }
            Result.success(Unit)
        } catch (e: Exception) {
            handleError(MusicPlayerError.UnknownError)
        }
    }

    override suspend fun setRepeatMode(enabled: Boolean): Result<Unit> = 
        withContext(Dispatchers.Main) {
            try {
                mediaController?.repeatMode = if (enabled) Player.REPEAT_MODE_ONE else Player.REPEAT_MODE_OFF
                updateState { it.copy(isRepeatMode = enabled) }
                Result.success(Unit)
            } catch (e: Exception) {
                handleError(MusicPlayerError.UnknownError)
            }
        }

    override fun getCurrentPosition(): Long = try {
        mediaController?.currentPosition ?: 0L
    } catch (e: IllegalStateException) {
        0L
    }

    private fun setupMediaController() {
        mediaControllerFuture.addListener({
            try {
                mediaController = mediaControllerFuture.get()
                mediaController?.addListener(mediaPlayerListener)
            } catch (e: Exception) {
                handleError(error = e as MusicPlayerError)
            }
        }, ContextCompat.getMainExecutor(context))
    }
    
    private fun createMediaItem(track: SunoTrackDataAppModel): MediaItem {
        return MediaItem.Builder()
            .setUri(track.audioUrl)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .setTitle(track.title)
                    .setArtist(track.id)
                    .setArtworkUri(track.imageUrl.toUri())
                    .build()
            )
            .build()
    }
    
    private fun mapPlayerState(playbackState: Int): PlaybackState {
        return when (playbackState) {
            Player.STATE_IDLE -> PlaybackState.IDLE
            Player.STATE_BUFFERING -> PlaybackState.BUFFERING
            Player.STATE_READY -> PlaybackState.READY
            Player.STATE_ENDED -> PlaybackState.ENDED
            else -> PlaybackState.ERROR
        }
    }
    
    private fun getDuration(currentState: MusicPlayerState): Long {
        return currentTrack?.duration?.let { (it * 1000).toLong() }
            ?: try { 
                mediaController?.duration ?: 0L 
            } catch (e: Exception) { 
                currentState.duration 
            }
    }
    
    private fun safeGetIsPlaying(): Boolean {
        return try { 
            mediaController?.isPlaying ?: false 
        } catch (e: Exception) { 
            false 
        }
    }
    
    private fun handleError(error: MusicPlayerError): Result<Unit> {
        updateState { it.copy(error = error) }
        return Result.failure(error)
    }
    
    private fun startPositionUpdates() {
        stopPositionUpdates()
        positionUpdateJob = CoroutineScope(Dispatchers.Main).launch {
            while (mediaController?.isPlaying == true) {
                updateState { it.copy(currentPosition = mediaController?.currentPosition ?: 0L) }
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