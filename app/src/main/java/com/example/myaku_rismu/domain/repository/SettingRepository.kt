package com.example.myaku_rismu.domain.repository

import com.example.myaku_rismu.domain.model.SettingData

interface SettingRepository {
    suspend fun getSetting(): SettingData
    suspend fun updateSettingState(selectType : Int, value : Int)
    suspend fun updateSettingState(selectType: Int, year: Int, month: Int, day: Int)
}