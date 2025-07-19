package com.example.myaku_rismu.core.di

import com.example.myaku_rismu.data.repository.ProfileDetailRepositoryImpl
import com.example.myaku_rismu.domain.repository.ProfileDetailRepository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.Binds

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindProfileDetailRepository(
        impl: ProfileDetailRepositoryImpl
    ): ProfileDetailRepository
}