package com.example.myaku_rismu.feature.library

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// TODO
val allTracks = listOf(
    MusicTrack(1, "Title by AI1", "2025.06.04", "Happy"),
    MusicTrack(2, "Title by AI2", "2025.06.04", "Sad"),
    MusicTrack(3, "Title by AI3", "2025.06.04", "Angry"),
    MusicTrack(4, "Title by AI4", "2025.06.04", "Happy"),
    MusicTrack(5, "Title by AI5", "2025.06.04", "Surprised"),
    MusicTrack(6, "Title by AI6", "2025.06.04", "Sad"),
    MusicTrack(7, "Title by AI7", "2025.06.05", "Happy"),
    MusicTrack(8, "Title by AI8", "2025.06.05", "Angry"),
)

class LibraryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryState())
    val uiState: StateFlow<LibraryState> = _uiState.asStateFlow()

    init {
        _uiState.update { currentState ->
            currentState.copy(tracks = filterTracks(currentState.selectedCategory))
        }
    }

    fun onEvent(event: LibraryUiEvent) {
        when (event) {
            is LibraryUiEvent.SelectCategory -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        selectedCategory = event.category,
                        tracks = filterTracks(event.category)
                    )
                }
            }
        }
    }

    private fun filterTracks(category: String): List<MusicTrack> {
        return if (category == "All") {
            allTracks
        } else {
            allTracks.filter { it.category == category }
        }
    }
}
