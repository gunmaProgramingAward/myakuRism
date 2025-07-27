package com.example.myaku_rismu.data.di

import com.example.myaku_rismu.data.repository.SunoRepositoryImpl
import com.example.myaku_rismu.data.useCase.SunoUseCaseImpl
import com.example.myaku_rismu.domain.repository.SunoRepository
import com.example.myaku_rismu.domain.useCase.SunoUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SunoModule {

    @Binds
    @Singleton
    abstract fun bindSunoRepository(
        sunoRepositoryImpl: SunoRepositoryImpl
    ): SunoRepository

    @Binds
    @Singleton
    abstract fun bindSunoUseCase(
        sunoUseCaseImpl: SunoUseCaseImpl
    ): SunoUseCase
}