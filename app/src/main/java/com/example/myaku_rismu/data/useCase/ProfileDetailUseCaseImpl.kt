package com.example.myaku_rismu.data.useCase

import com.example.myaku_rismu.data.model.ProfileSwitchType
import com.example.myaku_rismu.domain.repository.ProfileDetailRepository
import com.example.myaku_rismu.domain.useCase.ProfileDetailUseCase
import javax.inject.Inject

class ProfileDetailUseCaseImpl @Inject constructor(
    private val repository: ProfileDetailRepository
) : ProfileDetailUseCase {

    override suspend fun getProfileDetail() = repository.getProfileDetail()

    override suspend fun getSwitchState(switchType: ProfileSwitchType): Boolean {
        return repository.getSwitchState(switchType)
    }

    override suspend fun updateSwitchState(switchType: ProfileSwitchType, enabled: Boolean) {
        repository.updateSwitchState(switchType, enabled)
    }
}