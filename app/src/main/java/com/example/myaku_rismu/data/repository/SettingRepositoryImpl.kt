package com.example.myaku_rismu.data.repository

import com.example.myaku_rismu.data.datasource.SettingDataSource
import com.example.myaku_rismu.data.model.SettingType
import com.example.myaku_rismu.domain.model.ActivityLevel
import com.example.myaku_rismu.domain.model.Gender
import com.example.myaku_rismu.domain.repository.SettingRepository
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val dataSource: SettingDataSource
) : SettingRepository {

    override suspend fun getHeight(): Int? = dataSource.getHeight()
    override suspend fun getWeight(): Int? = dataSource.getWeight()
    override suspend fun getBirthYear(): Int? = dataSource.getBirthYear()
    override suspend fun getBirthMonth(): Int? = dataSource.getBirthMonth()
    override suspend fun getBirthDay(): Int? = dataSource.getBirthDay()
    override suspend fun getGender(): Gender? = dataSource.getGender()
    override suspend fun getActivityLevel(): ActivityLevel? = dataSource.getActivityLevel()
    override suspend fun getHeartRateTarget(): Int? = dataSource.getHeartRateTarget()
    override suspend fun getStepsTarget(): Int? = dataSource.getStepsTarget()
    override suspend fun getCaloriesTarget(): Int? = dataSource.getCaloriesTarget()
    override suspend fun getSleepTimeTarget(): Int? = dataSource.getSleepTimeTarget()
    override suspend fun getDistanceTarget(): Int? = dataSource.getDistanceTarget()

    override suspend fun updateHeightAndWeight(selectType: SettingType, value: Int) {
        dataSource.updateHeightAndWeight(selectType, value)
    }

    override suspend fun updateBirthdate(selectType: SettingType, year: Int, month: Int, day: Int) {
        dataSource.updateBirthdate(selectType, year, month, day)
    }

    override suspend fun updateGender(gender: Gender) {
        dataSource.updateGender(gender)
    }

    override suspend fun updateActivityLevel(level: ActivityLevel) {
        dataSource.updateActivityLevel(level)
    }

    override suspend fun updateHeartRateTarget(target: Int) {
        dataSource.updateHeartRateTarget(target)
    }

    override suspend fun updateStepsTarget(target: Int) {
        dataSource.updateStepsTarget(target)
    }

    override suspend fun updateCaloriesTarget(target: Int) {
        dataSource.updateCaloriesTarget(target)
    }

    override suspend fun updateSleepTimeTarget(target: Int) {
        dataSource.updateSleepTimeTarget(target)
    }

    override suspend fun updateDistanceTarget(target: Int) {
        dataSource.updateDistanceTarget(target)
    }
}