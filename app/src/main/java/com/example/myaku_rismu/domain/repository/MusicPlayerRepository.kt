package com.example.myaku_rismu.domain.repository

import com.example.myaku_rismu.domain.model.MusicPlayerState
import com.example.myaku_rismu.domain.model.SunoTrackDataAppModel
import kotlinx.coroutines.flow.Flow

interface MusicPlayerRepository {
    fun observePlayerState(): Flow<Result<MusicPlayerState>>
    suspend fun loadTrack(track: SunoTrackDataAppModel): Result<Unit>
    suspend fun play(): Result<Unit>
    suspend fun pause(): Result<Unit>
    suspend fun stop(): Result<Unit>
    suspend fun seekTo(position: Long): Result<Unit>
    suspend fun setRepeatMode(enabled: Boolean): Result<Unit>
    fun getCurrentPosition(): Long
}