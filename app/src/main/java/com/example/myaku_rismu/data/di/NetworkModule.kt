package com.example.myaku_rismu.data.di

import com.example.myaku_rismu.core.base.constants.SecretContents
import com.example.myaku_rismu.core.base.constants.SunoContents
import com.example.myaku_rismu.data.network.SunoApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${SecretContents.SUNO_API_KEY}")
                    .addHeader("Content-Type", "application/json")
                    .build()

                val response = chain.proceed(request)
                response
            }
            .connectTimeout(SunoContents.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(SunoContents.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(SunoContents.WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        val json = Json {
            ignoreUnknownKeys = true
        }

        return Retrofit.Builder()
            .baseUrl(SunoContents.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : SunoApiService {
        return retrofit.create(SunoApiService::class.java)
    }
}