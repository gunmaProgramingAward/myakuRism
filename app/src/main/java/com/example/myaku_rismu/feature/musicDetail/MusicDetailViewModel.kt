package com.example.myaku_rismu.feature.musicDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.data.model.RecordType
import com.example.myaku_rismu.domain.model.CreateMusicModel
import com.example.myaku_rismu.domain.model.MusicPlayerError
import com.example.myaku_rismu.domain.model.MusicPlayerState
import com.example.myaku_rismu.domain.model.SunoGenerateAppModel
import com.example.myaku_rismu.domain.model.SunoTaskDetailsAppModel
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
) : ViewModel() {
    private val _uiState = MutableStateFlow(MusicDetailState())
    val uiState: StateFlow<MusicDetailState> = _uiState.asStateFlow()
    val playerState: StateFlow<MusicPlayerState> =
        musicPlayerUseCase.observePlayerState()
            .map { result ->
                result.getOrElse { 
                    MusicPlayerState(
                        error = it as? MusicPlayerError
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = MusicPlayerState()
            )

    init {
        startMusicGeneration()
        _uiState.update { it.copy(screenState = ScreenState.Success(true)) }
    }
    
    private fun startMusicGeneration() {
        viewModelScope.launch {
            musicPlayerUseCase.clearError()
            _uiState.update {
                it.copy(
                    isLoading = LoadingState.NOTTING,
                    screenState = ScreenState.Initializing()
                )
            }
            
            val response: SunoGenerateAppModel? = generateMusic()
            val taskID = response?.taskId ?: return@launch

            var result = getMusic(taskID)
            while (result?.taskDetail?.status != LoadingState.SUCCESS.state) {
                // 10秒で20リクエストが制限のため
                delay(2000)

                result = getMusic(taskID)
                _uiState.update {
                    it.copy(
                        isLoading = LoadingState.fromStateString(
                            result?.taskDetail?.status ?: LoadingState.NOTTING.state
                        )
                    )
                }
            }
            loadAndPlayMusic(taskID = taskID)
        }
    }


    private suspend fun generateMusic(): SunoGenerateAppModel? {
        return try {
            sunoUseCase.generateMusic(
                CreateMusicModel(
                    recordType = RecordType.CALORIES,
                    bpm = 110,
                    isInstrumental = false,
                    currentDate = LocalDate.now().toString(),
                )
            )
        }catch (e: Exception) {
            _uiState.update {
                it.copy(screenState = ScreenState.Error(message = e.message))
            }
            null
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

    private suspend fun loadAndPlayMusic(taskID: String) {
        try {
            musicPlayerUseCase.clearError()
            
            val response = getMusic(taskID)
            val testTrack = response?.taskDetail?.response ?: return

            val loadResult = musicPlayerUseCase.loadTrack(testTrack)
            if (loadResult.isSuccess) {
                togglePlayPause()
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(screenState = ScreenState.Error(message = e.message))
            }
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
}