package com.example.myaku_rismu.data.useCase

import com.example.myaku_rismu.data.constants.GenreConstants
import com.example.myaku_rismu.data.constants.NegativeTabsConstants
import com.example.myaku_rismu.data.model.RecordType
import com.example.myaku_rismu.domain.model.ClassicSubGenreModel
import com.example.myaku_rismu.domain.model.CreateMusicModel
import com.example.myaku_rismu.domain.model.EDMSubGenreModel
import com.example.myaku_rismu.domain.model.HIPHOPSubGenreModel
import com.example.myaku_rismu.domain.model.ModelName
import com.example.myaku_rismu.domain.model.POPSSubGenreModel
import com.example.myaku_rismu.domain.model.SunoGenerateAppModel
import com.example.myaku_rismu.domain.model.SunoGenerateRequestAppModel
import com.example.myaku_rismu.domain.model.SunoTaskDetailsAppModel
import com.example.myaku_rismu.domain.repository.SunoRepository
import com.example.myaku_rismu.domain.useCase.SunoUseCase
import javax.inject.Inject

class SunoUseCaseImpl @Inject constructor(
    private val sunoRepository: SunoRepository
): SunoUseCase{
    override suspend fun generateMusic(request: CreateMusicModel): SunoGenerateAppModel {
        val sunoGenerateRequest = when (request.isInstrumental) {
            true -> SunoGenerateRequestAppModel.Instrumental(
                modelName = ModelName.V4_5PLUS,
                instrumental = true,
                negativeTags = getNegativeTags(request.recordType),
                style = getStyle(request.recordType, request.bpm),
                )
            false -> SunoGenerateRequestAppModel.Custom(
                modelName = ModelName.V4_5PLUS,
                instrumental = false,
                negativeTags = getNegativeTags(request.recordType),
                prompt = getPrompt(request.recordType),
                style = getStyle(request.recordType, request.bpm),
                title = request.currentDate,
            )
        }
        return sunoRepository.generateMusic(sunoGenerateRequest)
    }

    override suspend fun getMusic(taskId: String): SunoTaskDetailsAppModel {
        return sunoRepository.getMusic(taskId)
    }

    private fun getPrompt(recordType: RecordType): String = when (recordType) {
        RecordType.STEPS -> GenreConstants.STEPS
        RecordType.DISTANCE -> GenreConstants.DISTANCE
        RecordType.SLEEP_TIME -> GenreConstants.SLEEP_TIME
        RecordType.CALORIES -> GenreConstants.CALORIES
        else -> ""
    }

    private fun getStyle(recordType: RecordType, bpm: Int): String = when (recordType) {
        RecordType.STEPS -> "${bpm}bpm, ${HIPHOPSubGenreModel.random().description}"
        RecordType.DISTANCE -> "${bpm}bpm, ${EDMSubGenreModel.random().description}"
        RecordType.SLEEP_TIME -> "${bpm}bpm, ${ClassicSubGenreModel.random().description}"
        RecordType.CALORIES -> "${bpm}bpm, ${POPSSubGenreModel.random().description}"
        else -> ""
    }

    private fun getNegativeTags(recordType: RecordType): String = when (recordType) {
        RecordType.STEPS -> NegativeTabsConstants.STEP
        RecordType.DISTANCE -> NegativeTabsConstants.DISTANCE
        RecordType.SLEEP_TIME -> NegativeTabsConstants.SLEEP_TIME
        RecordType.CALORIES -> NegativeTabsConstants.CALORIES
        else -> ""
    }
}