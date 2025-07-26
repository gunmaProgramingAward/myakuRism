package com.example.myaku_rismu.domain.repository

import com.example.myaku_rismu.data.model.ProfileSwitchType

interface ProfileDetailRepository {
    suspend fun getIncludeLyricsSwitchState(): Boolean
    suspend fun getMusicGenerationNotificationSwitchState(): Boolean
    suspend fun getCollaborationWithHealthcareSwitchState(): Boolean
    suspend fun getSyncWithYourSmartwatchSwitchState(): Boolean
    suspend fun updateSwitchState(switchType: ProfileSwitchType, enabled: Boolean)
}