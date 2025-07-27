package com.example.myaku_rismu.domain.useCase

import com.example.myaku_rismu.domain.model.CreateMusicModel
import com.example.myaku_rismu.domain.model.SunoGenerateAppModel
import com.example.myaku_rismu.domain.model.SunoGenerateRequestAppModel
import com.example.myaku_rismu.domain.model.SunoTaskDetailsAppModel

interface SunoUseCase {
    suspend fun generateMusic(request: CreateMusicModel): SunoGenerateAppModel
    suspend fun getMusic(taskId: String): SunoTaskDetailsAppModel
}