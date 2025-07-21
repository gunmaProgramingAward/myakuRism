package com.example.myaku_rismu.data.useCase

import com.example.myaku_rismu.data.datasource.HealthConnectDataSource
import com.example.myaku_rismu.domain.repository.HealthConnectRepository
import javax.inject.Inject

class GetHeightUseCase @Inject constructor(
    private val repository: HealthConnectRepository
) {
    suspend operator fun invoke(): Double? = repository.getHeight()
}