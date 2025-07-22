package com.example.myaku_rismu.data.datasource

import com.example.myaku_rismu.data.model.SunoGenerateRequestResponse
import com.example.myaku_rismu.data.model.SunoGenerateResponse
import com.example.myaku_rismu.data.model.SunoTaskDetailsResponse

interface NetworkDataSource {
    suspend fun generateMusic(request: SunoGenerateRequestResponse): Result<SunoGenerateResponse>
    suspend fun getMusic(taskId: String): Result<SunoTaskDetailsResponse>
}