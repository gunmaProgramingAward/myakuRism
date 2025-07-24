package com.example.myaku_rismu.data.datasource

import com.example.myaku_rismu.core.di.IoDispatcher
import com.example.myaku_rismu.data.network.SunoApiService
import com.example.myaku_rismu.data.model.SunoGenerateRequestResponse
import com.example.myaku_rismu.data.model.SunoGenerateResponse
import com.example.myaku_rismu.data.model.SunoTaskDetailsResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class NetworkDataSourceImpl @Inject constructor(
    private val apiService: SunoApiService,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NetworkDataSource {
    override suspend fun generateMusic(request: SunoGenerateRequestResponse): SunoGenerateResponse =
        withContext(ioDispatcher) {
            try {
                val response = apiService.generateMusic(request)
                SunoGenerateResponse(
                    msg = response.msg,
                    code = response.code,
                    data = response.data
                )
            } catch (e: IOException) {
                throw Exception(e.message, e)
            } catch (e: Exception) {
                throw Exception(e.message, e)
            }
        }

    override suspend fun getMusic(taskId: String): SunoTaskDetailsResponse =
        withContext(ioDispatcher) {
            try {
                val response = apiService.getMusic(taskId)
                SunoTaskDetailsResponse(
                    code = response.code,
                    msg = response.msg,
                    data = response.data
                )
            } catch (e: IOException) {
                throw Exception(e.message, e)
            } catch (e: Exception) {
                throw Exception(e.message, e)
            }
        }
}