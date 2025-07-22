package com.example.myaku_rismu.domain.model

data class SunoGenerateAppModel(
    val isSuccess: Boolean,
    val message: String,
    val taskId: SunoTaskAppModel,
)

data class SunoTaskAppModel(
    val taskId: String
)
