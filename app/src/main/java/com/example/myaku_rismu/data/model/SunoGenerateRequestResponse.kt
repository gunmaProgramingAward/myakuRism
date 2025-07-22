package com.example.myaku_rismu.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SunoGenerateRequestResponse(
    @SerialName("prompt") val prompt: String,
    @SerialName("model") val model: String = "V3_5",
    @SerialName("customMode") val customMode: Boolean = true,
    @SerialName("instrumental") val instrumental: Boolean = true,
    @SerialName("style") val style: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("callBackUrl") val callBackUrl: String
)