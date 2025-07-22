package com.example.myaku_rismu.data.network

import com.example.myaku_rismu.domain.model.SunoGenerateRequest
import com.example.myaku_rismu.domain.model.SunoGenerateResponse
import com.example.myaku_rismu.domain.model.SunoTaskDetailsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SunoApiService {
    @POST("api/v1/generate")
    suspend fun generateMusic(
        @Body request: SunoGenerateRequest
    ): SunoGenerateResponse

    @GET("api/v1/generate/record-info")
    suspend fun getMusicGenerationDetails(
        @Query("task_id") taskId: String
    ): SunoTaskDetailsResponse

}