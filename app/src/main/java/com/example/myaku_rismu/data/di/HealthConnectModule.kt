package com.example.myaku_rismu.data.di

import com.example.myaku_rismu.data.repository.HealthConnectRepositoryImpl
import com.example.myaku_rismu.data.useCase.HealthConnectPermissionUseCaseImpl
import com.example.myaku_rismu.data.useCase.HealthConnectUseCaseImpl
import com.example.myaku_rismu.domain.repository.HealthConnectRepository
import com.example.myaku_rismu.domain.useCase.HealthConnectPermissionUseCase
import com.example.myaku_rismu.domain.useCase.HealthConnectUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HealthConnectModule {

    @Binds
    @Singleton
    abstract fun bindHealthConnectRepository(
        healthConnectRepositoryImpl: HealthConnectRepositoryImpl
    ): HealthConnectRepository

    @Binds
    @Singleton
    abstract fun bindHealthConnectPermissionUseCase(
        healthConnectPermissionUseCaseImpl: HealthConnectPermissionUseCaseImpl
    ): HealthConnectPermissionUseCase

    @Binds
    abstract fun bindHealthConnectUseCase(
        healthConnectUseCaseImpl: HealthConnectUseCaseImpl
    ): HealthConnectUseCase
}