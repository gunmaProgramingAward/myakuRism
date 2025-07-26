package com.example.myaku_rismu.domain.useCase

import com.example.myaku_rismu.data.model.ProfileSwitchType
import com.example.myaku_rismu.domain.model.ProfileDetailData

interface ProfileDetailUseCase {
    suspend fun getProfileDetail(): ProfileDetailData
    suspend fun getSwitchState(switchType: ProfileSwitchType): Boolean
    suspend fun updateSwitchState(switchType: ProfileSwitchType, enabled: Boolean)
}