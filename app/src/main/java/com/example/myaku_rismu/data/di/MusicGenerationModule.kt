package com.example.myaku_rismu.data.di

import com.example.myaku_rismu.data.repository.MusicGenerationRepositoryImpl
import com.example.myaku_rismu.data.useCase.MusicGenerationUseCaseImpl
import com.example.myaku_rismu.domain.repository.MusicGenerationRepository
import com.example.myaku_rismu.domain.useCase.MusicGenerationUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MusicGenerationModule {

    @Binds
    @Singleton
    abstract fun bindMusicGenerationRepository(
        musicGenerationRepositoryImpl: MusicGenerationRepositoryImpl
    ): MusicGenerationRepository

    @Binds
    @Singleton
    abstract fun bindMusicGenerationUseCase(
        musicGenerationUseCaseImpl: MusicGenerationUseCaseImpl
    ): MusicGenerationUseCase
}