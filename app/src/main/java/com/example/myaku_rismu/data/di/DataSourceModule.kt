package com.example.myaku_rismu.data.di

import android.content.Context
import com.example.myaku_rismu.data.datasource.HealthConnectDataSource
import com.example.myaku_rismu.data.datasource.HealthConnectDataSourceImpl
import com.example.myaku_rismu.data.datasource.ProfileDetailDataSource
import com.example.myaku_rismu.data.datasource.ProfileDetailDataSourceImpl
import com.example.myaku_rismu.data.datasource.SettingDataSource
import com.example.myaku_rismu.data.datasource.SettingDataSourceImpl
import com.example.myaku_rismu.data.datasource.NetworkDataSource
import com.example.myaku_rismu.data.datasource.NetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceBindModule {
    @Binds
    @Singleton
    abstract fun bindNetworkDataSource(
        networkDataSourceImpl: NetworkDataSourceImpl
    ): NetworkDataSource
}

@Module
@InstallIn(SingletonComponent::class)
object DataSourceProvideModule {
    @Provides
    @Singleton
    fun provideSettingDataSource(
        @ApplicationContext context: Context
    ): SettingDataSource {
        return SettingDataSourceImpl(context)
    }

    @Provides
    @Singleton
    fun provideProfileDetailDataSource(
        @ApplicationContext context: Context
    ): ProfileDetailDataSource {
        return ProfileDetailDataSourceImpl(context)
    }
}