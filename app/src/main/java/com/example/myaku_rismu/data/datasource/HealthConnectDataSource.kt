package com.example.myaku_rismu.data.datasource


interface HealthConnectDataSource {
    suspend fun fetchHeightRecords(): Double?
    suspend fun fetchWeightRecords(): Double?
    suspend fun fetchHeartRateRecords():List<Any>
}