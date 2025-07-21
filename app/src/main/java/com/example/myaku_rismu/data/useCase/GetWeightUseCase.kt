package com.example.myaku_rismu.data.useCase

import com.example.myaku_rismu.domain.repository.HealthConnectRepository
import javax.inject.Inject

class GetWeightUseCase @Inject constructor(
    private val repository: HealthConnectRepository
) {
    suspend operator fun invoke(): Double? = repository.getWeight()
}