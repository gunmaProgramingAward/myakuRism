package com.example.myaku_rismu.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SunoGenerateResponse(
    @SerialName("code") val code: Int,
    @SerialName("msg") val msg: String,
    @SerialName("data") val data: SunoTaskResponse? = null
)

@Serializable
data class SunoTaskResponse(
    @SerialName("taskId") val taskId: String
)