package com.example.myaku_rismu.feature.library

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// TODO
val allTracksViewModel = listOf(
    MusicTrack(1, "Title by AI1", "2025.06.04", MusicCategory.HAPPY),
    MusicTrack(2, "Title by AI2", "2025.06.04", MusicCategory.SAD),
    MusicTrack(3, "Title by AI3", "2025.06.04", MusicCategory.ANGRY),
    MusicTrack(4, "Title by AI4", "2025.06.04", MusicCategory.HAPPY),
    MusicTrack(5, "Title by AI5", "2025.06.04", MusicCategory.SURPRISED),
    MusicTrack(6, "Title by AI6", "2025.06.04", MusicCategory.SAD),
    MusicTrack(7, "Title by AI7", "2025.06.05", MusicCategory.HAPPY),
    MusicTrack(8, "Title by AI8", "2025.06.05", MusicCategory.ANGRY),
)

@HiltViewModel
class LibraryViewModel @Inject constructor() : ViewModel() {
    private fun getInitialLibraryState(): LibraryState {
        val initialSelectedCategory = MusicCategory.ALL
        val initialTracks = filterTracks(initialSelectedCategory, allTracksViewModel)
        return LibraryState(
            selectedCategory = initialSelectedCategory,
            tracks = initialTracks
        )
    }

    private val _uiState = MutableStateFlow(getInitialLibraryState())
    val uiState: StateFlow<LibraryState> = _uiState.asStateFlow()

    fun onEvent(event: LibraryUiEvent) {
        when (event) {
            is LibraryUiEvent.SelectCategory -> {
                _uiState.update { currentState ->
                    currentState.copy(
                        selectedCategory = event.category,
                        tracks = filterTracks(event.category, allTracksViewModel)
                    )
                }
            }
        }
    }

    private fun filterTracks(category: MusicCategory, tracksToFilter: List<MusicTrack>): List<MusicTrack> {
        return if (category == MusicCategory.ALL) {
            tracksToFilter
        } else {
            tracksToFilter.filter { it.category == category }
        }
    }
}
