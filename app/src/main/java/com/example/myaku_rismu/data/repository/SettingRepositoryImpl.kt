package com.example.myaku_rismu.data.repository

import com.example.myaku_rismu.data.datasource.SettingDataSource
import com.example.myaku_rismu.data.model.SettingType
import com.example.myaku_rismu.domain.model.ActivityLevel
import com.example.myaku_rismu.domain.model.Gender
import com.example.myaku_rismu.domain.model.SettingData
import com.example.myaku_rismu.domain.repository.SettingRepository
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val dataSource: SettingDataSource
) : SettingRepository {

    override suspend fun getSetting(): SettingData {
        return dataSource.getSetting()
    }

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
}