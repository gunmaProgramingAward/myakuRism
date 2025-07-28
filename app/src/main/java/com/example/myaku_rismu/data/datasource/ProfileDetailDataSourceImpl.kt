package com.example.myaku_rismu.data.datasource

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.myaku_rismu.core.base.MyakuRismuApplication
import com.example.myaku_rismu.core.base.constants.ProfileDetailPrefKeys
import com.example.myaku_rismu.data.model.ProfileSwitchType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ProfileDetailDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ProfileDetailDataSource {
    private val dataStore = (context.applicationContext as MyakuRismuApplication).dataStore

    private val includeLyricsSwitchKey =
        booleanPreferencesKey(ProfileDetailPrefKeys.INCLUDE_LYRICS_SWITCH)

    private val musicGenerationNotificationSwitchKey =
        booleanPreferencesKey(ProfileDetailPrefKeys.MUSIC_GENERATION_NOTIFICATION_SWITCH)

    private val collaborationWithHealthcareSwitchKey =
        booleanPreferencesKey(ProfileDetailPrefKeys.COLLABORATION_WITH_HEALTHCARE_SWITCH)

    private val syncWithYourSmartwatchSwitchKey =
        booleanPreferencesKey(ProfileDetailPrefKeys.SYNC_WITH_YOUR_SMARTWATCH_SWITCH)


    override suspend fun getIncludeLyricsSwitchState(): Boolean {
        return dataStore.data.first()[includeLyricsSwitchKey] ?: false
    }

    override suspend fun getMusicGenerationNotificationSwitchState(): Boolean {
        return dataStore.data.first()[musicGenerationNotificationSwitchKey] ?: false
    }

    override suspend fun getCollaborationWithHealthcareSwitchState(): Boolean {
        return dataStore.data.first()[collaborationWithHealthcareSwitchKey] ?: false
    }

    override suspend fun getSyncWithYourSmartwatchSwitchState(): Boolean {
        return dataStore.data.first()[syncWithYourSmartwatchSwitchKey] ?: false
    }


    override suspend fun updateSwitchState(switchType: ProfileSwitchType, enabled: Boolean) {
        dataStore.edit { preferences ->
            when (switchType) {
                ProfileSwitchType.INCLUDE_LYRICS ->
                    preferences[includeLyricsSwitchKey] = enabled

                ProfileSwitchType.MUSIC_GENERATION_NOTIFICATION ->
                    preferences[musicGenerationNotificationSwitchKey] = enabled

                ProfileSwitchType.COLLABORATION_WITH_HEALTHCARE ->
                    preferences[collaborationWithHealthcareSwitchKey] = enabled

                ProfileSwitchType.SYNC_WITH_YOUR_SMARTWATCH ->
                    preferences[syncWithYourSmartwatchSwitchKey] = enabled
            }
        }
    }
}