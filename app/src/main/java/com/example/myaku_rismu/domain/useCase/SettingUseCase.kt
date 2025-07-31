package com.example.myaku_rismu.domain.useCase

import com.example.myaku_rismu.data.model.RecordType
import com.example.myaku_rismu.data.model.SettingType
import com.example.myaku_rismu.domain.model.ActivityLevel
import com.example.myaku_rismu.domain.model.Gender
import com.example.myaku_rismu.domain.model.SettingData

interface SettingUseCase {
    suspend fun getHeight(): Int?
    suspend fun getWeight(): Int?
    suspend fun getBirthYear(): Int?
    suspend fun getBirthMonth(): Int?
    suspend fun getBirthDay(): Int?
    suspend fun getGender(): Gender?
    suspend fun getActivityLevel(): ActivityLevel?
    suspend fun getSetting(): SettingData
    suspend fun getHeartRateTarget(): Int?
    suspend fun getStepsTarget(): Int?
    suspend fun getCaloriesTarget(): Int?
    suspend fun getSleepTimeTarget(): Int?
    suspend fun getDistanceTarget(): Int?
    suspend fun getRecordTypeTarget(recordType: RecordType): Int?
    suspend fun updateHeightAndWeight(selectType: SettingType, value: Int)
    suspend fun updateBirthdate(selectType: SettingType, year: Int, month: Int, day: Int)
    suspend fun updateGender(gender: Gender)
    suspend fun updateActivityLevel(level: ActivityLevel)
    suspend fun updateHeartRateTarget(target: Int)
    suspend fun updateStepsTarget(target: Int)
    suspend fun updateCaloriesTarget(target: Int)
    suspend fun updateSleepTimeTarget(target: Int)
    suspend fun updateDistanceTarget(target: Int)
    suspend fun updateRecordTypeTarget(recordType: RecordType, target: Int)
    suspend fun calculateCaloriesTarget(): Int
}