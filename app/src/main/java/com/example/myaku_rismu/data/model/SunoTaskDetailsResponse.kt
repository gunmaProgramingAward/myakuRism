package com.example.myaku_rismu.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SunoTaskDetailsResponse(
    @SerialName("code") val code: Int,
    @SerialName("msg") val msg: String,
    @SerialName("data") val data: SunoTaskDetailResponse? = null
)

@Serializable
data class SunoTaskDetailResponse(
    @SerialName("taskId") val taskId: String,
    @SerialName("parentMusicId") val parentMusicId: String? = null,
    @SerialName("param") val param: String? = null,
    @SerialName("response") val response: SunoResponseDataResponse? = null,
    @SerialName("status") val status: String,
    @SerialName("type") val type: String? = null,
    @SerialName("operationType") val operationType: String? = null,
    @SerialName("errorCode") val errorCode: String? = null,
    @SerialName("errorMessage") val errorMessage: String? = null,
    @SerialName("createTime") val createTime: Long? = null
)

@Serializable
data class SunoResponseDataResponse(
    @SerialName("taskId") val taskId: String,
    @SerialName("sunoData") val sunoData: List<SunoTrackDataResponse>? = null
)

@Serializable
data class SunoTrackDataResponse(
    @SerialName("id") val id: String,
    @SerialName("audioUrl") val audioUrl: String? = null,
    @SerialName("sourceAudioUrl") val sourceAudioUrl: String? = null,
    @SerialName("streamAudioUrl") val streamAudioUrl: String? = null,
    @SerialName("sourceStreamAudioUrl") val sourceStreamAudioUrl: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("sourceImageUrl") val sourceImageUrl: String? = null,
    @SerialName("prompt") val prompt: String,
    @SerialName("modelName") val modelName: String,
    @SerialName("title") val title: String,
    @SerialName("tags") val tags: String,
    @SerialName("createTime") val createTime: Long,
    @SerialName("duration") val duration: Double? = null
) {
    val bestAudioUrl: String? get() = audioUrl ?: sourceAudioUrl
    val bestStreamUrl: String? get() = streamAudioUrl ?: sourceStreamAudioUrl
    val bestImageUrl: String? get() = imageUrl ?: sourceImageUrl
}