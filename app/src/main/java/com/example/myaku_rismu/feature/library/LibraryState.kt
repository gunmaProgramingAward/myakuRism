package com.example.myaku_rismu.feature.library

data class LibraryState(
    val categories: List<String> = listOf("All", "Happy", "Sad", "Angry", "Surprised"),
    val selectedCategory: String = "All",
    val tracks: List<MusicTrack> = allTracks
)
