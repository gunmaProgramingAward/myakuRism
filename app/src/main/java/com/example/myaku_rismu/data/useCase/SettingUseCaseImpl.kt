package com.example.myaku_rismu.data.useCase

import com.example.myaku_rismu.data.model.RecordType
import com.example.myaku_rismu.data.model.SettingType
import com.example.myaku_rismu.domain.model.ActivityLevel
import com.example.myaku_rismu.domain.model.Gender
import com.example.myaku_rismu.domain.model.SettingData
import com.example.myaku_rismu.domain.repository.SettingRepository
import com.example.myaku_rismu.domain.useCase.SettingUseCase
import java.util.Calendar
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

    override suspend fun getHeartRateTarget(): Int? = repository.getHeartRateTarget()
    override suspend fun getStepsTarget(): Int? = repository.getStepsTarget()
    override suspend fun getCaloriesTarget(): Int? = repository.getCaloriesTarget()
    override suspend fun getSleepTimeTarget(): Int? = repository.getSleepTimeTarget()
    override suspend fun getDistanceTarget(): Int? = repository.getDistanceTarget()
    override suspend fun getRecordTypeTarget(recordType: RecordType): Int? {
        return when (recordType) {
            RecordType.HEART_RATE -> repository.getHeartRateTarget()
            RecordType.STEPS -> repository.getStepsTarget()
            RecordType.CALORIES -> repository.getCaloriesTarget()
            RecordType.SLEEP_TIME -> repository.getSleepTimeTarget()
            RecordType.DISTANCE -> repository.getDistanceTarget()
        }
    }

    override suspend fun updateHeightAndWeight(selectType: SettingType, value: Int) =
        repository.updateHeightAndWeight(selectType, value)

    override suspend fun updateBirthdate(selectType: SettingType, year: Int, month: Int, day: Int) =
        repository.updateBirthdate(selectType, year, month, day)

    override suspend fun updateGender(gender: Gender) =
        repository.updateGender(gender)

    override suspend fun updateActivityLevel(level: ActivityLevel) =
        repository.updateActivityLevel(level)

    override suspend fun updateHeartRateTarget(target: Int) {
        repository.updateHeartRateTarget(target)
    }
    override suspend fun updateStepsTarget(target: Int) {
        repository.updateStepsTarget(target)
    }
    override suspend fun updateCaloriesTarget(target: Int) {
        repository.updateCaloriesTarget(target)
    }
    override suspend fun updateSleepTimeTarget(target: Int)  {
        repository.updateSleepTimeTarget(target)
    }
    override suspend fun updateDistanceTarget(target: Int)  {
        repository.updateDistanceTarget(target)
    }

    override suspend fun updateRecordTypeTarget(recordType: RecordType, target: Int) {
        when (recordType) {
            RecordType.HEART_RATE -> repository.updateHeartRateTarget(target)
            RecordType.STEPS -> repository.updateStepsTarget(target)
            RecordType.CALORIES -> repository.updateCaloriesTarget(target)
            RecordType.SLEEP_TIME -> repository.updateSleepTimeTarget(target)
            RecordType.DISTANCE -> repository.updateDistanceTarget(target)
        }
    }

    override suspend fun calculateCaloriesTarget(): Int {
        val heightCm = repository.getHeight()
        val weightKg = repository.getWeight()
        val birthYear = repository.getBirthYear()
        val gender = repository.getGender()
        val activityLevel = repository.getActivityLevel()

        if (heightCm == null || weightKg == null || birthYear == null || gender == null || activityLevel == null) {
            return 0
        }

        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val age = currentYear - birthYear

        val bmr = when (gender) {
            Gender.MALE -> 13.397 * weightKg + 4.799 * heightCm - 5.677 * age + 88.362
            Gender.FEMALE -> 9.247 * weightKg + 3.098 * heightCm - 4.330 * age + 447.593
            Gender.OTHER -> 11.322* weightKg + 3.9485 * heightCm - 5.0035 * age + 267.977
        }

        val activityFactor = when (activityLevel) {
            ActivityLevel.LOW -> 1.2
            ActivityLevel.MEDIUM -> 1.55
            ActivityLevel.HIGH -> 1.725
        }

        val calories = bmr * activityFactor

        return calories.toInt()
    }
}