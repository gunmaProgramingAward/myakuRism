package com.example.myaku_rismu.data.mapper

import com.example.myaku_rismu.data.model.SunoResponseDataResponse
import com.example.myaku_rismu.data.model.SunoTaskDetailResponse
import com.example.myaku_rismu.data.model.SunoTaskDetailsResponse
import com.example.myaku_rismu.data.model.SunoTrackDataResponse
import com.example.myaku_rismu.domain.model.SunoTaskDetailAppModel
import com.example.myaku_rismu.domain.model.SunoTaskDetailsAppModel
import com.example.myaku_rismu.domain.model.SunoTrackDataAppModel
import javax.inject.Inject

class SunoTaskDetailsMapper @Inject constructor() {

    fun toAppModel(response: SunoTaskDetailsResponse): SunoTaskDetailsAppModel {
        return SunoTaskDetailsAppModel(
            statusCode = response.code,
            message = response.msg,
            taskDetail = mapTaskDetail(response.data)
        )
    }

    private fun mapTaskDetail(detail: SunoTaskDetailResponse?): SunoTaskDetailAppModel {
        return SunoTaskDetailAppModel(
            taskId = detail?.taskId ?: "",
            parentMusicId = detail?.parentMusicId ?: "",
            param = detail?.param ?: "",
            response = detail?.response?.sunoData
                ?.maxByOrNull { it.duration ?: 0.0 }
                ?.let { sunoTrackData(it, detail.response) },
            status = detail?.status ?: "",
            type = detail?.type ?: "",
            operationType = detail?.operationType ?: "",
            errorCode = detail?.errorCode,
            errorMessage = detail?.errorMessage,
            createTime = detail?.createTime ?: 0L
        )
    }

    private fun sunoTrackData(
        track: SunoTrackDataResponse,
        sunoResponseDataResponse: SunoResponseDataResponse?
    ): SunoTrackDataAppModel {
        return SunoTrackDataAppModel(
            id = track.id,
            taskId = sunoResponseDataResponse?.taskId ?: "",
            audioUrl = track.bestAudioUrl ?: "",
            imageUrl = track.bestImageUrl ?: "",
            modelName = track.modelName,
            title = track.title,
            tags = parseTagsFromString(track.tags),
            createTime = track.createTime,
            duration = track.duration ?: 0.0,
            prompt = track.prompt
        )
    }

    private fun parseTagsFromString(tagsString: String): List<String> {
        return if (tagsString.isBlank()) {
            emptyList()
        } else {
            tagsString.split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
        }
    }
}