package com.example.myaku_rismu.feature.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myaku_rismu.data.datasource.NetworkDataSource
import com.example.myaku_rismu.data.model.RecordType
import com.example.myaku_rismu.data.model.SunoGenerateRequestResponse
import com.example.myaku_rismu.domain.model.CreateMusicModel
import com.example.myaku_rismu.domain.model.ModelName
import com.example.myaku_rismu.domain.model.SunoGenerateRequestAppModel
import com.example.myaku_rismu.domain.useCase.SunoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sunoUseCase: SunoUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState.asStateFlow()
    var taskId: String = "12345"

    fun generateMusic() {
        viewModelScope.launch {
            val result = sunoUseCase.generateMusic(
                CreateMusicModel(
                    recordType = RecordType.STEPS,
                    isInstrumental = true,
                    currentDate = Instant.now().toString(),
                    bpm = 40
                )
            )
            taskId = result.taskId.toString()
            Log.d("result", "generateMusic: $result")

            while (result.statusCode == 200) {
                delay(2000)
                val musicResult = sunoUseCase.getMusic(taskId)
                Log.d("result", "getMusic: ${musicResult.taskDetail.status}")
                if (musicResult.taskDetail.status == "SUCCESS") {
                    Log.d("result", "getMusic: ${musicResult.taskDetail.response.sunoData}")
                    break
                }
            }
        }
    }

    fun getMusic(taskId: String) {
        viewModelScope.launch {
            val result = sunoUseCase.getMusic(taskId)
            Log.d("result", "getMusic: ${result.statusCode}")
            Log.d("result", "getMusic: ${result.message}")
            Log.d("result", "getMusic: ${result.taskDetail.status}")
            Log.d("result", "getMusic: ${result.taskDetail.response.sunoData}")
        }
    }
}