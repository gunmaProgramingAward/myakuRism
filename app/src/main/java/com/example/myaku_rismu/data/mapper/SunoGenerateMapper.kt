package com.example.myaku_rismu.data.mapper

import com.example.myaku_rismu.core.base.constants.SecretContents
import com.example.myaku_rismu.data.model.SunoGenerateRequestResponse
import com.example.myaku_rismu.data.model.SunoGenerateResponse
import com.example.myaku_rismu.domain.model.SunoGenerateAppModel
import com.example.myaku_rismu.domain.model.SunoGenerateRequestAppModel
import javax.inject.Inject

class SunoGenerateMapper @Inject constructor() {
    fun toResponse(response: SunoGenerateRequestAppModel) : SunoGenerateRequestResponse {
        return when (response) {
            is SunoGenerateRequestAppModel.Custom -> SunoGenerateRequestResponse(
                prompt = response.prompt,
                model = response.modelName.model,
                customMode = true,
                instrumental = response.instrumental,
                style = response.style,
                title = response.title,
                negativeTags = response.negativeTags,
                callBackUrl = SecretContents.CALL_BACK_URL
            )
            is SunoGenerateRequestAppModel.Normal -> SunoGenerateRequestResponse(
                prompt = response.prompt,
                model = response.modelName.model,
                customMode = false,
                instrumental = response.instrumental,
                style = null,
                title = null,
                negativeTags = response.negativeTags,
                callBackUrl = SecretContents.CALL_BACK_URL
            )
            is SunoGenerateRequestAppModel.Instrumental -> SunoGenerateRequestResponse(
                prompt = null,
                model = response.modelName.model,
                customMode = true,
                instrumental = true,
                style = response.style,
                title = null,
                negativeTags = response.negativeTags,
                callBackUrl = SecretContents.CALL_BACK_URL
            )
        }
    }

    fun toAppModel(response: SunoGenerateResponse): SunoGenerateAppModel {
        return SunoGenerateAppModel(
            statusCode = response.code,
            message = response.msg,
            taskId = response.data?.taskId
        )
    }
}