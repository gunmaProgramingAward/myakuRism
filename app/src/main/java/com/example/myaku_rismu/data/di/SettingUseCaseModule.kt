package com.example.myaku_rismu.data.di

import com.example.myaku_rismu.data.repository.SettingRepositoryImpl
import com.example.myaku_rismu.data.useCase.SettingUseCaseImpl
import com.example.myaku_rismu.domain.repository.SettingRepository
import com.example.myaku_rismu.domain.useCase.SettingUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingUseCaseModule {
    @Binds
    @Singleton
    abstract fun bindSettingRepository(
        impl: SettingRepositoryImpl
    ): SettingRepository

    @Binds
    @Singleton
    abstract fun bindSettingUseCase(
        impl: SettingUseCaseImpl
    ): SettingUseCase
}