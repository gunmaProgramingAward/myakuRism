package com.example.myaku_rismu.data.repository

import com.example.myaku_rismu.data.datasource.ProfileDetailDataSource
import com.example.myaku_rismu.data.model.ProfileSwitchType
import com.example.myaku_rismu.domain.repository.ProfileDetailRepository
import javax.inject.Inject


class ProfileDetailRepositoryImpl @Inject constructor(
   private val dataSource: ProfileDetailDataSource
) : ProfileDetailRepository {

    override suspend fun getIncludeLyricsSwitchState(): Boolean {
        return dataSource.getIncludeLyricsSwitchState()
    }

    override suspend fun getMusicGenerationNotificationSwitchState(): Boolean {
        return dataSource.getMusicGenerationNotificationSwitchState()
    }

    override suspend fun getCollaborationWithHealthcareSwitchState(): Boolean {
        return dataSource.getCollaborationWithHealthcareSwitchState()
    }

    override suspend fun getSyncWithYourSmartwatchSwitchState(): Boolean {
        return dataSource.getSyncWithYourSmartwatchSwitchState()
    }

    override suspend fun updateSwitchState(switchType: ProfileSwitchType, enabled: Boolean) {
        dataSource.updateSwitchState(switchType, enabled)
    }
}