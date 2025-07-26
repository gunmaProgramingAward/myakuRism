package com.example.myaku_rismu.data.datasource

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.myaku_rismu.core.base.MyakuRismuApplication
import com.example.myaku_rismu.core.base.constants.SettingPrefKeys
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
    private val dataStore
        get() = (context.applicationContext as MyakuRismuApplication).dataStore

    private val heightKey = intPreferencesKey(SettingPrefKeys.HEIGHT_KEY)
    private val weightKey = intPreferencesKey(SettingPrefKeys.WEIGHT_KEY)
    private val birthYearKey = intPreferencesKey(SettingPrefKeys.BIRTH_YEAR_KEY)
    private val birthMonthKey = intPreferencesKey(SettingPrefKeys.BIRTH_MONTH_KEY)
    private val birthdayKey = intPreferencesKey(SettingPrefKeys.BIRTH_DAY_KEY)
    private val genderKey = intPreferencesKey(SettingPrefKeys.GENDER_KEY)
    private val activityLevelKey = intPreferencesKey(SettingPrefKeys.ACTIVITY_LEVEL_KEY)


    override suspend fun getHeight(): Int? = dataStore.data.first()[heightKey]
    override suspend fun getWeight(): Int? = dataStore.data.first()[weightKey]
    override suspend fun getBirthYear(): Int? = dataStore.data.first()[birthYearKey]
    override suspend fun getBirthMonth(): Int? = dataStore.data.first()[birthMonthKey]
    override suspend fun getBirthDay(): Int? = dataStore.data.first()[birthdayKey]
    override suspend fun getGender(): Gender? = Gender.fromId(dataStore.data.first()[genderKey])
    override suspend fun getActivityLevel(): ActivityLevel? = ActivityLevel.fromId(dataStore.data.first()[activityLevelKey])

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