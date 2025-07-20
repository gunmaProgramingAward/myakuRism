package com.example.myaku_rismu.data.datasource

interface HealthConnectDataSource {
    suspend fun fetchHeartRateRecords():List<Any>
}