package com.example.myaku_rismu.feature.library

sealed class LibraryUiEvent {
    data class SelectCategory(val category: MusicCategory) : LibraryUiEvent()
}
