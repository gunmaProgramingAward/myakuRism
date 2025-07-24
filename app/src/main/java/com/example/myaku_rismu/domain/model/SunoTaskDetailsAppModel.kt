package com.example.myaku_rismu.domain.model

data class SunoTaskDetailsAppModel(
    val statusCode: Int,
    val message: String,
    val taskDetail: SunoTaskDetailAppModel,
)

data class SunoTaskDetailAppModel(
    val taskId: String,
    val parentMusicId: String,
    val param: String,
    val response: SunoResponseDateAppModel,
    val status: String,
    val type: String,
    val operationType: String,
    val errorCode: String?,
    val errorMessage: String?,
    val createTime: Long,
)

data class SunoResponseDateAppModel(
    val taskId: String,
    val sunoData: List<SunoTrackDataAppModel>,
)

data class SunoTrackDataAppModel(
    val id: String,
    val audioUrl: String,
    val sourceAudioUrl: String,
    val streamAudioUrl: String,
    val sourceStreamAudioUrl: String,
    val imageUrl: String,
    val sourceImageUrl: String,
    val modelName: String,
    val title: String,
    val tags: List<String>,
    val createTime: Long,
    val duration: Double,
    val prompt: String
)