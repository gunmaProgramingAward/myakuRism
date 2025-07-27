package com.example.myaku_rismu.domain.useCase

import com.example.myaku_rismu.data.model.ProfileSwitchType
import com.example.myaku_rismu.feature.profileDetail.ProfileDetailData

interface ProfileDetailUseCase {
    suspend fun getIncludeLyricsSwitchState(): Boolean
    suspend fun getMusicGenerationNotificationSwitchState(): Boolean
    suspend fun getCollaborationWithHealthcareSwitchState(): Boolean
    suspend fun getSyncWithYourSmartwatchSwitchState(): Boolean
    suspend fun getProfileDetail(): ProfileDetailData
    suspend fun updateSwitchState(switchType: ProfileSwitchType, enabled: Boolean)
}