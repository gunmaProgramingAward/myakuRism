package com.example.myaku_rismu.data.datasource

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.myaku_rismu.data.model.SettingType
import com.example.myaku_rismu.domain.model.ActivityLevel
import com.example.myaku_rismu.domain.model.Gender
import com.example.myaku_rismu.domain.model.SettingData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class SettingDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingDataSource {
    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("settings") },
    )

    private val heightKey = intPreferencesKey("heightCm")
    private val weightKey = intPreferencesKey("weightKg")
    private val birthYearKey = intPreferencesKey("birthYear")
    private val birthMonthKey = intPreferencesKey("birthMonth")
    private val birthdayKey = intPreferencesKey("birthday")
    private val genderKey = intPreferencesKey("gender")
    private val activityLevelKey = intPreferencesKey("activityLevel")

    override suspend fun getSetting(): SettingData {
        val prefs = dataStore.data.first()

        return SettingData(
            heightCm = prefs[heightKey],
            weightKg = prefs[weightKey],
            birthYear = prefs[birthYearKey],
            birthMonth = prefs[birthMonthKey],
            birthDay = prefs[birthdayKey],
            gender = Gender.fromId(prefs[genderKey]),
            activityLevel = ActivityLevel.fromId(prefs[activityLevelKey])
        )
    }

    override suspend fun updateHeightAndWeight(
        selectType: SettingType,
        value: Int
    ) {
        dataStore.edit { prefs ->
            when (selectType) {
                SettingType.HEIGHT -> prefs[heightKey] = value
                SettingType.WEIGHT -> prefs[weightKey] = value
                else -> {}
            }
        }
    }

    override suspend fun updateBirthdate(
        selectType: SettingType,
        year: Int,
        month: Int,
        day: Int
    ) {
        dataStore.edit { prefs ->
            prefs[birthYearKey] = year
            prefs[birthMonthKey] = month
            prefs[birthdayKey] = day
        }
    }

    override suspend fun updateGender(gender: Gender) {
        dataStore.edit { prefs ->
            prefs[genderKey] = gender.id
        }
    }

    override suspend fun updateActivityLevel(level: ActivityLevel) {
        dataStore.edit { prefs ->
            prefs[activityLevelKey] = level.id
        }
    }
}