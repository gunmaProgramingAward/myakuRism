package com.example.myaku_rismu.data.datasource

import com.example.myaku_rismu.core.di.IoDispatcher
import com.example.myaku_rismu.data.network.SunoApiService
import com.example.myaku_rismu.data.model.SunoGenerateRequestResponse
import com.example.myaku_rismu.data.model.SunoGenerateResponse
import com.example.myaku_rismu.data.model.SunoTaskDetailsResponse
import com.example.myaku_rismu.data.model.SunoTaskResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val apiService: SunoApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NetworkDataSource {
    override suspend fun generateMusic(request: SunoGenerateRequestResponse)
            : Result<SunoGenerateResponse> = withContext(ioDispatcher) {
        try {
            val response = apiService.generateMusic(request)
            if (response.code == 200 && response.data != null) {
                val generateResponse = SunoGenerateResponse(
                    msg = response.msg,
                    code = response.code,
                    data = response.data
                )
                Result.success(generateResponse)
            } else {
                Result.failure(Exception(response.msg))
            }
        } catch (e: IOException) {
            Result.failure(Exception(e.message, e))
        } catch (e: Exception) {
            Result.failure(Exception(e.message, e))
        }
    }

    override suspend fun getMusic(taskId: String): Result<SunoTaskDetailsResponse> =
        withContext(ioDispatcher) {
            try {
                val response = apiService.getMusic(taskId)
                if (response.code == 200 && response.data != null) {
                    val taskDetails = SunoTaskDetailsResponse(
                        code = response.code,
                        msg = response.msg,
                        data = response.data
                    )
                    Result.success(taskDetails)
                } else {
                    Result.failure(Exception(response.msg))
                }
            } catch (e: IOException) {
                Result.failure(Exception(e.message, e))
            } catch (e: Exception) {
                Result.failure(Exception(e.message, e))
            }
        }
}