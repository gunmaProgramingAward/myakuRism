package com.example.myaku_rismu.data.network

import com.example.myaku_rismu.data.model.SunoGenerateRequestResponse
import com.example.myaku_rismu.data.model.SunoGenerateResponse
import com.example.myaku_rismu.data.model.SunoTaskDetailsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SunoApiService {
    @POST("api/v1/generate")
    suspend fun generateMusic(
        @Body request: SunoGenerateRequestResponse
    ): SunoGenerateResponse

    @GET("api/v1/generate/record-info")
    suspend fun getMusic(
        @Query("taskId") taskId: String
    ): SunoTaskDetailsResponse

}