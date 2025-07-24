package com.example.myaku_rismu.data.mapper

import com.example.myaku_rismu.data.model.SunoResponseDataResponse
import com.example.myaku_rismu.data.model.SunoTaskDetailResponse
import com.example.myaku_rismu.data.model.SunoTaskDetailsResponse
import com.example.myaku_rismu.data.model.SunoTrackDataResponse
import com.example.myaku_rismu.domain.model.SunoResponseDateAppModel
import com.example.myaku_rismu.domain.model.SunoTaskDetailAppModel
import com.example.myaku_rismu.domain.model.SunoTaskDetailsAppModel
import com.example.myaku_rismu.domain.model.SunoTrackDataAppModel
import javax.inject.Inject

class SunoTaskDetailsMapper @Inject constructor() {

    // TODO: 後で修正する
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
            response = mapResponseData(detail?.response),
            status = detail?.status ?: "",
            type = detail?.type ?: "",
            operationType = detail?.operationType ?: "",
            errorCode = detail?.errorCode,
            errorMessage = detail?.errorMessage,
            createTime = detail?.createTime ?: 0L
        )
    }

    private fun mapResponseData(response: SunoResponseDataResponse?): SunoResponseDateAppModel {
        return SunoResponseDateAppModel(
            taskId = response?.taskId ?: "",
            sunoData = response?.sunoData?.map { mapTrackData(it) } ?: emptyList()
        )
    }

    private fun mapTrackData(track: SunoTrackDataResponse): SunoTrackDataAppModel {
        return SunoTrackDataAppModel(
            id = track.id,
            audioUrl = track.bestAudioUrl ?: "", // bestAudioUrlを優先使用
            sourceAudioUrl = track.sourceAudioUrl ?: "",
            streamAudioUrl = track.bestStreamUrl ?: "", // bestStreamUrlを優先使用
            sourceStreamAudioUrl = track.sourceStreamAudioUrl ?: "",
            imageUrl = track.bestImageUrl ?: "", // bestImageUrlを優先使用
            sourceImageUrl = track.sourceImageUrl ?: "",
            modelName = track.modelName,
            title = track.title,
            tags = parseTagsFromString(track.tags), // String -> List<String>変換
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