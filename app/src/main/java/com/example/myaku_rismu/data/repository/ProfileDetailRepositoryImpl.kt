package com.example.myaku_rismu.data.repository

import com.example.myaku_rismu.data.datasource.ProfileDetailDataSource
import com.example.myaku_rismu.data.model.ProfileSwitchType
import com.example.myaku_rismu.domain.model.ProfileDetailData
import com.example.myaku_rismu.domain.repository.ProfileDetailRepository
import javax.inject.Inject


class ProfileDetailRepositoryImpl @Inject constructor(
   private val dataSource: ProfileDetailDataSource
) : ProfileDetailRepository {

    override suspend fun getProfileDetail(): ProfileDetailData {
        return dataSource.getProfileDetail()
    }

    override suspend fun getSwitchState(switchType: ProfileSwitchType): Boolean {
        return dataSource.getSwitchState(switchType)
    }

    override suspend fun updateSwitchState(switchType: ProfileSwitchType, enabled: Boolean) {
        dataSource.updateSwitchState(switchType, enabled)
    }
}