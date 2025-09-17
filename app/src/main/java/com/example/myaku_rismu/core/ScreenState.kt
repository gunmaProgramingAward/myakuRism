package com.example.myaku_rismu.core

sealed class ScreenState {
    data object Initializing : ScreenState()
    data object Success : ScreenState()
    data class Error(val message: String? = null) : ScreenState()
}