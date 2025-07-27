package com.example.myaku_rismu.data.repository

import com.example.myaku_rismu.data.datasource.NetworkDataSource
import com.example.myaku_rismu.data.mapper.SunoGenerateMapper
import com.example.myaku_rismu.data.mapper.SunoTaskDetailsMapper
import com.example.myaku_rismu.domain.model.SunoGenerateAppModel
import com.example.myaku_rismu.domain.model.SunoGenerateRequestAppModel
import com.example.myaku_rismu.domain.model.SunoTaskDetailsAppModel
import com.example.myaku_rismu.domain.repository.SunoRepository
import javax.inject.Inject

class SunoRepositoryImpl @Inject constructor(
    private val networkDataSource: NetworkDataSource,
    private val sunoGenerateMapper: SunoGenerateMapper,
    private val sunoTaskDetailMapper: SunoTaskDetailsMapper,
): SunoRepository {
    override suspend fun generateMusic(request: SunoGenerateRequestAppModel): SunoGenerateAppModel {
        val responseModel = sunoGenerateMapper.toResponse(request)
        val response = networkDataSource.generateMusic(responseModel)
        return sunoGenerateMapper.toAppModel(response)
    }

    override suspend fun getMusic(taskId: String): SunoTaskDetailsAppModel {
        val response = networkDataSource.getMusic(taskId)
        return sunoTaskDetailMapper.toAppModel(response)
    }
}