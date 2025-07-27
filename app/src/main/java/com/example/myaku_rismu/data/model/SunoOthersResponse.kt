package com.example.myaku_rismu.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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