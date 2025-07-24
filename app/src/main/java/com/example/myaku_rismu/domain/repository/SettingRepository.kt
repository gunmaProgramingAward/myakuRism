package com.example.myaku_rismu.domain.repository

import com.example.myaku_rismu.data.model.SettingType
import com.example.myaku_rismu.domain.model.ActivityLevel
import com.example.myaku_rismu.domain.model.SettingData

interface SettingRepository {
    suspend fun getSetting(): SettingData
    suspend fun updateSettingState(selectType : SettingType, value : Int)
    suspend fun updateSettingState(selectType: SettingType, year: Int, month: Int, day: Int)
    suspend fun updateActivityLevel(level: ActivityLevel)
}