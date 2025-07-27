package com.example.myaku_rismu.data.di

import com.example.myaku_rismu.data.repository.ProfileDetailRepositoryImpl
import com.example.myaku_rismu.data.useCase.ProfileDetailUseCaseImpl
import com.example.myaku_rismu.domain.repository.HealthConnectRepository
import com.example.myaku_rismu.domain.repository.ProfileDetailRepository
import com.example.myaku_rismu.domain.useCase.ProfileDetailUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileDetailUseCaseModule {
    @Binds
    @Singleton
    abstract fun bindProfileDetailRepository(
        profileDetailRepositoryImpl: ProfileDetailRepositoryImpl
    ): ProfileDetailRepository

    @Binds
    abstract fun bindProfileDetailUseCase(
        profileDetailUseCaseImpl: ProfileDetailUseCaseImpl
    ): ProfileDetailUseCase
}