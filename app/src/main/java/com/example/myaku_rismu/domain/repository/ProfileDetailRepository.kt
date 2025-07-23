package com.example.myaku_rismu.domain.repository

import com.example.myaku_rismu.domain.model.ProfileDetailData

interface ProfileDetailRepository {
    suspend fun getProfileDetail(): ProfileDetailData
    suspend fun updateSwitchState(switchType: Int, enabled: Boolean)
}