package com.example.myaku_rismu.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SunoGenerateRequestResponse(
    @SerialName("prompt") val prompt: String?,
    @SerialName("model") val model: String ,
    @SerialName("customMode") val customMode: Boolean,
    @SerialName("instrumental") val instrumental: Boolean,
    @SerialName("style") val style: String?,
    @SerialName("title") val title: String?,
    @SerialName("negativeTags") val negativeTags: String?,
    @SerialName("callBackUrl") val callBackUrl: String
)