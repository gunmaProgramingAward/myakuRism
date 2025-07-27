package com.example.myaku_rismu.core

sealed class ScreenState(open val process: Boolean) {
    class Initializing(override val process: Boolean = false) : ScreenState(process)
    class Success(override val process: Boolean = false) : ScreenState(process)
    class Error(
        override val process: Boolean = false,
        val message: String? = null,
        val messageResId: Int? = null
    ) : ScreenState(process)
}