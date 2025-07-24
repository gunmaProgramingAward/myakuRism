package com.example.myaku_rismu.domain.useCase

import com.example.myaku_rismu.data.model.SettingType
import com.example.myaku_rismu.domain.model.ActivityLevel
import com.example.myaku_rismu.domain.model.SettingData

interface SettingUseCase {
    suspend fun getSetting(): SettingData
    suspend fun updateSetting(selectType: SettingType, value: Int)
    suspend fun updateBirthdate(selectType: SettingType, year: Int, month: Int, day: Int)
    suspend fun updateActivityLevel(level: ActivityLevel)
}