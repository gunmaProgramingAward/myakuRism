package com.example.myaku_rismu.domain.useCase

import com.example.myaku_rismu.domain.model.MusicPlayerState
import com.example.myaku_rismu.domain.model.SunoTrackDataAppModel
import kotlinx.coroutines.flow.Flow

interface MusicPlayerUseCase {
    fun observePlayerState(): Flow<Result<MusicPlayerState>>
    suspend fun loadTrack(track: SunoTrackDataAppModel): Result<Unit>
    suspend fun loadAndPlayTrack(track: SunoTrackDataAppModel): Result<Unit>
    suspend fun togglePlayPause(): Result<Unit>
    suspend fun seekToPercentage(percentage: Float): Result<Unit>
    suspend fun toggleRepeatMode(): Result<Unit>
    fun getCurrentPosition(): Long
    suspend fun getProgressPercentage(): Float

    suspend fun getCurrentTimeFormatted(): String
    suspend fun getDurationFormatted(): String
    suspend fun getRemainingTimeFormatted(): String
    suspend fun isReadyToPlay(): Boolean
    suspend fun clearError(): Result<Unit>
}