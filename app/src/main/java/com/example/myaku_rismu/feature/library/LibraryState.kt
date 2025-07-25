package com.example.myaku_rismu.feature.library

data class LibraryState(
    val categories: List<MusicCategory> = MusicCategory.entries,
    val selectedCategory: MusicCategory = MusicCategory.ALL,
    val tracks: List<MusicTrack> = emptyList()
)

data class MusicTrack(
    val id: Int,
    val title: String,
    val date: String,
    val category: MusicCategory
)