package com.example.myaku_rismu.data.datasource

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.myaku_rismu.data.model.ProfileSwitchType
import com.example.myaku_rismu.domain.model.ProfileDetailData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ProfileDetailDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ProfileDetailDataSource {
    private val dataStore = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("profile_detail") },
    )
    private val includeLyricsSwitchKey =
        booleanPreferencesKey("include_lyrics_switch")

    private val musicGenerationNotificationSwitchKey =
        booleanPreferencesKey("music_generation_notification_switch")

    private val collaborationWithHealthcareSwitchKey =
        booleanPreferencesKey("collaboration_with_healthcare_switch")

    private val syncWithYourSmartwatchSwitchKey =
        booleanPreferencesKey("sync_with_your_smartwatch_switch")


    override suspend fun getProfileDetail(): ProfileDetailData {
        val preferences = dataStore.data.first()

        return ProfileDetailData(
            includeLyricsSwitchEnabled =
                preferences[includeLyricsSwitchKey] ?: false,

            musicGenerationNotificationSwitchEnabled =
                preferences[musicGenerationNotificationSwitchKey] ?: false,

            collaborationWithHealthcareSwitchEnabled =
                preferences[collaborationWithHealthcareSwitchKey] ?: false,

            syncWithYourSmartwatchSwitchEnabled =
                preferences[syncWithYourSmartwatchSwitchKey] ?: false
        )
    }

    override suspend fun getSwitchState(switchType: ProfileSwitchType): Boolean {
        val preferences = dataStore.data.first()

        return when (switchType) {
            ProfileSwitchType.INCLUDE_LYRICS ->
                preferences[includeLyricsSwitchKey] ?: false

            ProfileSwitchType.MUSIC_GENERATION_NOTIFICATION ->
                preferences[musicGenerationNotificationSwitchKey] ?: false

            ProfileSwitchType.COLLABORATION_WITH_HEALTHCARE ->
                preferences[collaborationWithHealthcareSwitchKey] ?: false

            ProfileSwitchType.SYNC_WITH_YOUR_SMARTWATCH ->
                preferences[syncWithYourSmartwatchSwitchKey] ?: false
        }
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