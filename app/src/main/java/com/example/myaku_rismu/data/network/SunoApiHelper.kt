package com.example.myaku_rismu.data.network

import com.example.myaku_rismu.domain.model.ApiResult
import com.example.myaku_rismu.domain.model.SunoGenerateRequest
import com.example.myaku_rismu.domain.model.SunoTaskResponse
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SunoApiHelper @Inject constructor() {
    private val apiKey: String = "05a0e31ffee5e9aeb05d077fff085926"
    private val baseUrl = "https://api.sunoapi.org/"
    
    private val retrofit: Retrofit by lazy {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            encodeDefaults = true  // デフォルト値も送信
            explicitNulls = false  // null値は送信しない
        }
        
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $apiKey")
                    .addHeader("Content-Type", "application/json")
                    .build()
                
                println("Request: ${request.url}")
                val response = chain.proceed(request)
                println("Response: ${response.code}")
                
                response
            }
            .build()
        
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }
    
    private val apiService: SunoApiService by lazy {
        retrofit.create(SunoApiService::class.java)
    }

    suspend fun generateMusic(prompt: String): ApiResult<SunoTaskResponse> = withContext(Dispatchers.IO) {
        try {
            if (apiKey.isEmpty()) {
                return@withContext ApiResult.Error(Exception("API key not set"))
            }

            val request = SunoGenerateRequest(
                prompt = prompt,
                model = "V4",
                customMode = true,  // trueに変更してstyleとtitleを必須にする
                instrumental = false,
                style = "pop",      // customMode=trueの場合必須
                title = "Generated Song", // customMode=trueの場合必須
                callBackUrl = "https://webhook.site/your-dummy-id"
            )

            println("Sending request to: api/v1/generate")
            println("Request body: $request")

            val response = apiService.generateMusic(request)

            println("Response received: code=${response.code}, msg=${response.msg}")

            if (response.code == 200 && response.data != null) {
                println("Success! taskId=${response.data.taskId}")
                ApiResult.Success(response.data)
            } else {
                println("API Error: ${response.msg}")
                ApiResult.Error(Exception("API Error: ${response.msg}"))
            }
        } catch (e: IOException) {
            println("Network Error: ${e.message}")
            ApiResult.Error(e)
        } catch (e: Exception) {
            println("Unexpected Error: ${e.message}")
            ApiResult.Error(e)
        }
    }
}