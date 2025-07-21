package com.example.myaku_rismu.data.repository

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.myaku_rismu.domain.model.SettingData
import com.example.myaku_rismu.domain.repository.SettingRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "setting")

class SettingRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingRepository {
    private val dataStore = context.dataStore

    private val heightKey = intPreferencesKey("heightCm")
    private val weightKey = intPreferencesKey("weightKg")
    private val genderKey = intPreferencesKey("gender")
    private val birthYearKey = intPreferencesKey("birthYear")
    private val birthMonthKey = intPreferencesKey("birthMonth")
    private val birthdayKey = intPreferencesKey("birthday")

    override suspend fun getSetting(): SettingData {
        val prefs = dataStore.data.first()
        return SettingData(
            heightCm = prefs[heightKey],
            weightKg = prefs[weightKey],
            gender = prefs[genderKey],
            birthYear = prefs[birthYearKey],
            birthMonth = prefs[birthMonthKey],
            birthDay = prefs[birthdayKey]
        )
    }

    override suspend fun updateSettingState(selectType: Int, value: Int) {
        dataStore.edit { prefs ->
            when (selectType) {
                0 -> { // Default value if not set
                    prefs[heightKey] = value
                }

                1 -> {
                    prefs[weightKey] = value
                }

                2 -> {
                    prefs[genderKey] = value
                }
            }
        }
    }

    override suspend fun updateSettingState(selectType: Int, year: Int, month: Int, day: Int) {
        dataStore.edit { prefs ->
            if (selectType == 3) {
                prefs[birthYearKey] = year
                prefs[birthMonthKey] = month
                prefs[birthdayKey] = day
            }
        }
    }
}