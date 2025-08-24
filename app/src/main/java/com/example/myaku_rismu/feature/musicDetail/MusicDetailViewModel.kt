package com.example.myaku_rismu.feature.musicDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.data.model.RecordType
import com.example.myaku_rismu.domain.model.CreateMusicModel
import com.example.myaku_rismu.domain.model.MusicPlayerError
import com.example.myaku_rismu.domain.model.MusicPlayerState
import com.example.myaku_rismu.domain.model.SunoTaskDetailsAppModel
import com.example.myaku_rismu.domain.useCase.MusicGenerationUseCase
import com.example.myaku_rismu.domain.useCase.MusicPlayerUseCase
import com.example.myaku_rismu.domain.useCase.SunoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MusicDetailViewModel @Inject constructor(
    private val sunoUseCase: SunoUseCase,
    private val musicPlayerUseCase: MusicPlayerUseCase,
    private val musicGenerationUseCase: MusicGenerationUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MusicDetailState())
    val uiState: StateFlow<MusicDetailState> = _uiState.asStateFlow()
    private val _playerState = MutableStateFlow(MusicPlayerState())
    val playerState: StateFlow<MusicPlayerState> = _playerState.asStateFlow()

    init {
        observeTodayMusicGeneration()
        initializedPlayerState()
        _uiState.update { it.copy(screenState = ScreenState.Success(true)) }
    }

    private fun initializedPlayerState() {
        viewModelScope.launch {
            musicPlayerUseCase.observePlayerState()
                .map { result ->
                    result.getOrElse {
                        MusicPlayerState(
                            error = it as? MusicPlayerError
                        )
                    }
                }
                .collect { state ->
                    _playerState.value = state
                }
        }
    }

    fun startMusicGeneration(
        recordType: RecordType,
        bpm: Int,
        instrumental: Boolean
    ) {
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isLoading = LoadingState.NOTTING,
                        screenState = ScreenState.Initializing(),
                        isCreatedMusic = true
                    )
                }

                val taskId = generateMusicTask(recordType, bpm, instrumental)
                    ?: return@launch

                waitForMusicCompletion(taskId)
                loadGeneratedMusic(taskId)

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(screenState = ScreenState.Error(message = e.message))
                }
            }
        }
    }

    private suspend fun generateMusicTask(
        recordType: RecordType,
        bpm: Int,
        instrumental: Boolean,
    ): String? {
        return try {
            val response = sunoUseCase.generateMusic(
                CreateMusicModel(
                    recordType = recordType,
                    bpm = bpm,
                    isInstrumental = instrumental,
                    currentDate = LocalDate.now().toString(),
                )
            )
            response.taskId
        } catch (e: Exception) {
            _uiState.update {
                it.copy(screenState = ScreenState.Error(message = e.message))
            }
            null
        }
    }

    private suspend fun waitForMusicCompletion(taskId: String) {
        var result = getMusic(taskId)
        while (result?.taskDetail?.status != LoadingState.SUCCESS.state) {
            delay(2000) // 10秒で20リクエストが制限のため

            result = getMusic(taskId)
            _uiState.update {
                it.copy(
                    isLoading = LoadingState.fromStateString(
                        result?.taskDetail?.status ?: LoadingState.PENDING.state
                    )
                )
            }
        }
    }

    private suspend fun getMusic(taskID: String): SunoTaskDetailsAppModel? {
        return try {
            sunoUseCase.getMusic(taskId = taskID)
        } catch (e: Exception) {
            _uiState.update {
                it.copy(screenState = ScreenState.Error(message = e.message))
            }
            null
        }
    }

    fun togglePlayPause() {
        viewModelScope.launch {
            musicPlayerUseCase.togglePlayPause()
        }
    }

    fun seekToPosition(position: Float) {
        viewModelScope.launch {
            musicPlayerUseCase.seekToPercentage(position)
        }
    }

    fun toggleRepeatMode() {
        viewModelScope.launch {
            musicPlayerUseCase.toggleRepeatMode()
        }
    }

    fun changeIsFavorite(boolean: Boolean) {
        _uiState.update {
            it.copy(
                isFavorite = boolean
            )
        }
    }

    fun changePlayerState(playerState: PlayerState) {
        _uiState.update {
            it.copy(
                playerState = playerState
            )
        }
    }

    fun changeMusicImagePausedRotation(rotation: Float) {
        _uiState.update {
            it.copy(
                musicImagePausedRotation = it.musicImagePausedRotation + rotation
            )
        }
    }

    fun changeDragOffset(offset: Float) {
        _uiState.update {
            it.copy(
                dragOffset = offset
            )
        }
    }

    fun plusDragOffset(offset: Float) {
        _uiState.update {
            it.copy(
                dragOffset = it.dragOffset + offset
            )
        }
    }

    private fun observeTodayMusicGeneration() {
        viewModelScope.launch {
            musicGenerationUseCase.getTodayGenerationFlow().collect { entity ->
                if (entity != null) {
                    _uiState.update { it.copy(isCreatedMusic = true) }
                    loadExistingMusic(entity.targetId)
                } else {
                    _uiState.update { it.copy(isCreatedMusic = false) }
                }
            }
        }
    }

    private suspend fun loadExistingMusic(targetId: String) {
        try {
            musicPlayerUseCase.clearError()

            val response = getMusic(targetId)
            val testTrack = response?.taskDetail?.response ?: return

            val loadResult = musicPlayerUseCase.loadTrack(testTrack)
            if (loadResult.isSuccess) {
                _uiState.update { it.copy(screenState = ScreenState.Success(true)) }
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(screenState = ScreenState.Error(message = e.message))
            }
        }
    }

    private suspend fun loadGeneratedMusic(taskId: String) {
        try {
            musicPlayerUseCase.clearError()

            val response = getMusic(taskId)
            val testTrack = response?.taskDetail?.response ?: return

            val loadResult = musicPlayerUseCase.loadTrack(testTrack)
            if (loadResult.isSuccess) {
                musicGenerationUseCase.saveTodayGeneration(taskId)
                _uiState.update {
                    it.copy(
                        isLoading = LoadingState.SUCCESS,
                        screenState = ScreenState.Success(true)
                    )
                }
                togglePlayPause()
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(screenState = ScreenState.Error(message = e.message))
            }
        }
    }
}