package com.example.myaku_rismu.domain.repository

import com.example.myaku_rismu.data.model.SettingType
import com.example.myaku_rismu.domain.model.ActivityLevel
import com.example.myaku_rismu.domain.model.Gender

interface SettingRepository {
    suspend fun getHeight(): Int?
    suspend fun getWeight(): Int?
    suspend fun getBirthYear(): Int?
    suspend fun getBirthMonth(): Int?
    suspend fun getBirthDay(): Int?
    suspend fun getGender(): Gender?
    suspend fun getActivityLevel(): ActivityLevel?
    suspend fun updateHeightAndWeight(selectType : SettingType, value : Int)
    suspend fun updateBirthdate(selectType: SettingType, year: Int, month: Int, day: Int)
    suspend fun updateGender(gender: Gender)
    suspend fun updateActivityLevel(level: ActivityLevel)
}