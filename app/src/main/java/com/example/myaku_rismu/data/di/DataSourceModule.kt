package com.example.myaku_rismu.data.di

import android.content.Context
import com.example.myaku_rismu.data.datasource.HealthConnectDataSource
import com.example.myaku_rismu.data.datasource.HealthConnectDataSourceImpl
import com.example.myaku_rismu.data.datasource.SettingDataSource
import com.example.myaku_rismu.data.datasource.SettingDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideHealthConnectDataSource(
        @ApplicationContext context: Context
    ): HealthConnectDataSource {
        return HealthConnectDataSourceImpl(context)
    }

    @Provides
    @Singleton
    fun provideSettingDataSource(
        @ApplicationContext context: Context
    ): SettingDataSource {
        return SettingDataSourceImpl(context)
    }
}