package com.example.myaku_rismu.data.useCase

import com.example.myaku_rismu.data.model.ProfileSwitchType
import com.example.myaku_rismu.domain.repository.ProfileDetailRepository
import com.example.myaku_rismu.domain.useCase.ProfileDetailUseCase
import com.example.myaku_rismu.feature.profileDetail.ProfileDetailData
import javax.inject.Inject

class ProfileDetailUseCaseImpl @Inject constructor(
    private val repository: ProfileDetailRepository
) : ProfileDetailUseCase {

    override suspend fun getIncludeLyricsSwitchState(): Boolean {
        return repository.getIncludeLyricsSwitchState()
    }
    override suspend fun getMusicGenerationNotificationSwitchState(): Boolean {
        return repository.getMusicGenerationNotificationSwitchState()
    }
    override suspend fun getCollaborationWithHealthcareSwitchState(): Boolean {
        return repository.getCollaborationWithHealthcareSwitchState()
    }
    override suspend fun getSyncWithYourSmartwatchSwitchState(): Boolean {
        return repository.getSyncWithYourSmartwatchSwitchState()
    }
    override suspend fun getProfileDetail(): ProfileDetailData {
        return ProfileDetailData(
            includeLyricsSwitchEnabled = repository.getIncludeLyricsSwitchState(),
            musicGenerationNotificationSwitchEnabled = repository.getMusicGenerationNotificationSwitchState(),
            collaborationWithHealthcareSwitchEnabled = repository.getCollaborationWithHealthcareSwitchState(),
            syncWithYourSmartwatchSwitchEnabled = repository.getSyncWithYourSmartwatchSwitchState()
        )
    }

    override suspend fun updateSwitchState(switchType: ProfileSwitchType, enabled: Boolean) {
        repository.updateSwitchState(switchType, enabled)
    }
}