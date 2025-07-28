package com.example.myaku_rismu.domain.model


data class MusicPlayerState(
    val currentTrack: SunoTrackDataAppModel? = null,
    val playbackState: PlaybackState = PlaybackState.IDLE,
    val isPlaying: Boolean = false,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val isRepeatMode: Boolean = false,
    val isShuffleMode: Boolean = false,
    val isFavorite: Boolean = false,
    val error: MusicPlayerError? = null
) {
    val progressPercentage: Float
        get() = if (duration > 0) {
            currentPosition.toFloat() / duration.toFloat()
        } else 0f
}

enum class PlaybackState {
    IDLE,
    BUFFERING,
    READY,
    PLAYING,
    PAUSED,
    ENDED,
    ERROR
}

sealed class MusicPlayerError : Exception() {
    data object NetworkError : MusicPlayerError()
    data object FileNotFound : MusicPlayerError()
    data object UnknownError : MusicPlayerError()
    data class ServiceConnectionError(override val message: String) : MusicPlayerError()
}