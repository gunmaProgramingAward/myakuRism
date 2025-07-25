package com.example.myaku_rismu.domain.repository

import com.example.myaku_rismu.domain.model.SunoGenerateAppModel
import com.example.myaku_rismu.domain.model.SunoGenerateRequestAppModel
import com.example.myaku_rismu.domain.model.SunoTaskDetailsAppModel

interface SunoRepository {
    suspend fun generateMusic(request: SunoGenerateRequestAppModel): SunoGenerateAppModel
    suspend fun getMusic(taskId: String): SunoTaskDetailsAppModel
}