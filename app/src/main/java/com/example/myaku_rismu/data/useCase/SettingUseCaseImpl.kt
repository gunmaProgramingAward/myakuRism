package com.example.myaku_rismu.data.useCase

import com.example.myaku_rismu.data.model.SettingType
import com.example.myaku_rismu.domain.model.ActivityLevel
import com.example.myaku_rismu.domain.model.Gender
import com.example.myaku_rismu.domain.model.SettingData
import com.example.myaku_rismu.domain.repository.SettingRepository
import com.example.myaku_rismu.domain.useCase.SettingUseCase
import javax.inject.Inject

class SettingUseCaseImpl @Inject constructor(
    private val repository: SettingRepository
) : SettingUseCase {
    override suspend fun getHeight(): Int? = repository.getHeight()
    override suspend fun getWeight(): Int? = repository.getWeight()
    override suspend fun getBirthYear(): Int? = repository.getBirthYear()
    override suspend fun getBirthMonth(): Int? = repository.getBirthMonth()
    override suspend fun getBirthDay(): Int? = repository.getBirthDay()
    override suspend fun getGender(): Gender? = repository.getGender()
    override suspend fun getActivityLevel(): ActivityLevel? = repository.getActivityLevel()
    override suspend fun getSetting(): SettingData {
        return SettingData(
            heightCm = repository.getHeight(),
            weightKg = repository.getWeight(),
            birthYear = repository.getBirthYear(),
            birthMonth = repository.getBirthMonth(),
            birthDay = repository.getBirthDay(),
            gender = repository.getGender(),
            activityLevel = repository.getActivityLevel()
        )
    }

    override suspend fun updateHeightAndWeight(selectType: SettingType, value: Int) =
        repository.updateHeightAndWeight(selectType, value)

    override suspend fun updateBirthdate(selectType: SettingType, year: Int, month: Int, day: Int) =
        repository.updateBirthdate(selectType, year, month, day)

    override suspend fun updateGender(gender: Gender) =
        repository.updateGender(gender)

    override suspend fun updateActivityLevel(level: ActivityLevel) =
        repository.updateActivityLevel(level)

}