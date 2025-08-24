package com.example.myaku_rismu.core

import com.example.myaku_rismu.data.model.RecordType

data class AppStateModel(
    val musicGenerationTrigger: MusicGenerationTrigger? = null,
)

data class MusicGenerationTrigger(
    val recordType: RecordType,
    val bpm: Int,
    val instrumental : Boolean,
    val timestamp: Long,
)