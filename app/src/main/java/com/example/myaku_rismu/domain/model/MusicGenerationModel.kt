package com.example.myaku_rismu.domain.model

import com.example.myaku_rismu.data.model.RecordType

data class MusicGenerationTrigger(
    val recordType: RecordType?,
    val bpm: Int,
    val instrumental : Boolean,
    val timestamp: Long,
)