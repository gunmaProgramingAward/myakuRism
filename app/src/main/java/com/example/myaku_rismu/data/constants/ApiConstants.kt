package com.example.myaku_rismu.data.constants

object ApiConstants {
    const val BASE_URL = "https://api.example.com/v1/"
    const val TIMEOUT_SECONDS = 30L
    
    object Endpoints {
        const val MUSIC_RECOMMENDATIONS = "music/recommendations"
        const val HEALTH_METRICS = "health/metrics"
        const val HEALTH_ANALYSIS = "health/analysis"
        const val MUSIC_GENERATE = "music/generate"
    }
    
    object Headers {
        const val CONTENT_TYPE = "Content-Type"
        const val AUTHORIZATION = "Authorization"
        const val API_KEY = "X-API-Key"
    }
}