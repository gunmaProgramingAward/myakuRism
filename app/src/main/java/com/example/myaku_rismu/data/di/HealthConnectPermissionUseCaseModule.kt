package com.example.myaku_rismu.data.di

import com.example.myaku_rismu.data.datasource.HealthConnectDataSource
import com.example.myaku_rismu.data.datasource.HealthConnectDataSourceImpl
import com.example.myaku_rismu.data.repository.HealthConnectRepositoryImpl
import com.example.myaku_rismu.data.useCase.HealthConnectPermissionUseCaseImpl
import com.example.myaku_rismu.domain.repository.HealthConnectRepository
import com.example.myaku_rismu.domain.useCase.HealthConnectPermissionUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HealthConnectPermissionUseCaseModule {

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
    abstract fun bindHealthConnectDataSource(
        impl: HealthConnectDataSourceImpl
    ): HealthConnectDataSource
}