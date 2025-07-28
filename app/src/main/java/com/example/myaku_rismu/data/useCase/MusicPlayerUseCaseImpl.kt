package com.example.myaku_rismu.data.useCase

import com.example.myaku_rismu.domain.model.MusicPlayerError
import com.example.myaku_rismu.domain.model.MusicPlayerState
import com.example.myaku_rismu.domain.model.PlaybackState
import com.example.myaku_rismu.domain.model.SunoTrackDataAppModel
import com.example.myaku_rismu.domain.repository.MusicPlayerRepository
import com.example.myaku_rismu.domain.useCase.MusicPlayerUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.Locale
import javax.inject.Inject

class MusicPlayerUseCaseImpl @Inject constructor(
    private val repository: MusicPlayerRepository
) : MusicPlayerUseCase {

    override fun observePlayerState(): Flow<Result<MusicPlayerState>> {
        return repository.observePlayerState()
    }

    override suspend fun loadTrack(track: SunoTrackDataAppModel): Result<Unit> {
        return repository.loadTrack(track)
    }

    override suspend fun loadAndPlayTrack(track: SunoTrackDataAppModel): Result<Unit> {
        val loadResult = repository.loadTrack(track)

        return loadResult.fold(
            onSuccess = {
                kotlinx.coroutines.delay(200)
                val playResult = repository.play()
                playResult
            },
            onFailure = {
                Result.failure(it)
            }
        )
    }

    override suspend fun togglePlayPause(): Result<Unit> {
        return repository.observePlayerState().first().fold(
            onSuccess = { state ->
                when (state.playbackState) {
                    PlaybackState.PLAYING -> {
                        repository.pause()
                    }
                    PlaybackState.PAUSED, PlaybackState.READY -> {
                        repository.play()
                    }
                    PlaybackState.ENDED -> {
                        repository.play()
                    }
                    else -> {
                        Result.failure(MusicPlayerError.UnknownError)
                    }
                }
            },
            onFailure = { Result.failure(it) }
        )
    }

    override suspend fun seekToPercentage(percentage: Float): Result<Unit> {
        return repository.observePlayerState().first().fold(
            onSuccess = { state ->
                val position = (state.duration * percentage.coerceIn(0f, 1f)).toLong()
                repository.seekTo(position)
            },
            onFailure = { Result.failure(it) }
        )
    }


    override suspend fun toggleRepeatMode(): Result<Unit> {
        return repository.observePlayerState().first().fold(
            onSuccess = { state ->
                repository.setRepeatMode(!state.isRepeatMode)
            },
            onFailure = { Result.failure(it) }
        )
    }

    override fun getCurrentPosition(): Long {
        return repository.getCurrentPosition()
    }

    override suspend fun getProgressPercentage(): Float {
        return repository.observePlayerState().first().fold(
            onSuccess = { state ->
                if (state.duration > 0) {
                    state.currentPosition.toFloat() / state.duration.toFloat()
                } else {
                    0f
                }
            },
            onFailure = { 0f }
        )
    }

    override suspend fun getCurrentTimeFormatted(): String {
        return repository.observePlayerState().first().fold(
            onSuccess = { state ->
                formatTime(state.currentPosition)
            },
            onFailure = { "0:00" }
        )
    }

    override suspend fun getDurationFormatted(): String {
        return repository.observePlayerState().first().fold(
            onSuccess = { state ->
                formatTime(state.duration)
            },
            onFailure = { "0:00" }
        )
    }

    override suspend fun getRemainingTimeFormatted(): String {
        return repository.observePlayerState().first().fold(
            onSuccess = { state ->
                val remaining = state.duration - state.currentPosition
                formatTime(remaining)
            },
            onFailure = { "0:00" }
        )
    }

    private fun formatTime(timeMs: Long): String {
        val totalSeconds = timeMs / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.JAPAN,"%d:%02d", minutes, seconds)
    }

    override suspend fun isReadyToPlay(): Boolean {
        return repository.observePlayerState().first().fold(
            onSuccess = { state ->
                state.playbackState in listOf(
                    PlaybackState.READY,
                    PlaybackState.PLAYING,
                    PlaybackState.PAUSED
                )
            },
            onFailure = { false }
        )
    }

    override suspend fun clearError(): Result<Unit> {
        return when {
            isReadyToPlay() -> Result.success(Unit)
            else -> repository.stop()
        }
    }
}