package com.example.myaku_rismu.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.myaku_rismu.domain.model.ProfileDetailData
import com.example.myaku_rismu.domain.repository.ProfileDetailRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "profile_detail")

class ProfileDetailRepositoryImpl @Inject constructor(
   @ApplicationContext private val context: Context
) : ProfileDetailRepository {
    private val dataStore = context.dataStore

    override suspend fun getProfileDetail(): ProfileDetailData {
        val prefs = dataStore.data.first()
        return ProfileDetailData(
            includeLyricsSwitchEnabled = prefs[booleanPreferencesKey("includeLyrics")] ?: false,
            musicGenerationNotificationSwitchEnabled = prefs[booleanPreferencesKey("musicGenerationNotification")] ?: false,
            collaborationWithHealthcareSwitchEnabled = prefs[booleanPreferencesKey("collaborationWithHealthcare")] ?: false,
            syncWithYourSmartwatchSwitchEnabled = prefs[booleanPreferencesKey("syncWithYourSmartwatch")] ?: false
        )
    }

    override suspend fun updateSwitchState(switchType: Int, enabled: Boolean) {
        dataStore.edit { prefs ->
            when (switchType) {
                0 -> prefs[booleanPreferencesKey("includeLyrics")] = enabled
                1 -> prefs[booleanPreferencesKey("musicGenerationNotification")] = enabled
                2 -> prefs[booleanPreferencesKey("collaborationWithHealthcare")] = enabled
                3 -> prefs[booleanPreferencesKey("syncWithYourSmartwatch")] = enabled
            }
        }
    }
}