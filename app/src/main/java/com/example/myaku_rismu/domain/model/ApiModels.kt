package com.example.myaku_rismu.domain.model

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
    @SerialName("task_id") val taskId: String,
    @SerialName("status") val status: String? = null,
    @SerialName("create_time") val createTime: String? = null,
    @SerialName("update_time") val updateTime: String? = null
)

@Serializable
data class SunoAudioResponse(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("lyric") val lyric: String? = null,
    @SerialName("audio_url") val audioUrl: String? = null,
    @SerialName("video_url") val videoUrl: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("model_name") val modelName: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("gpt_description_prompt") val gptDescriptionPrompt: String? = null,
    @SerialName("prompt") val prompt: String? = null,
    @SerialName("type") val type: String? = null,
    @SerialName("tags") val tags: String? = null,
    @SerialName("duration") val duration: Double? = null
)

@Serializable
data class SunoLyricsResponse(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("text") val text: String,
    @SerialName("status") val status: String
)

@Serializable
data class SunoTaskDetailsResponse(
    @SerialName("code") val code: Int,
    @SerialName("msg") val msg: String,
    @SerialName("data") val data: SunoTaskDetailResponse? = null
)

@Serializable
data class SunoTaskDetailResponse(
    @SerialName("task_id") val taskId: String,
    @SerialName("status") val status: String,
    @SerialName("data") val audioList: List<SunoAudioResponse>? = null,
    @SerialName("create_time") val createTime: String? = null,
    @SerialName("update_time") val updateTime: String? = null
)

@Serializable
data class SunoGenerateRequest(
    @SerialName("prompt") val prompt: String,
    @SerialName("model") val model: String = "V3_5",
    @SerialName("customMode") val customMode: Boolean = true,
    @SerialName("instrumental") val instrumental: Boolean = true,
    @SerialName("style") val style: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("callBackUrl") val callBackUrl: String
)

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val exception: Throwable) : ApiResult<Nothing>()
    data object Loading : ApiResult<Nothing>()
}