package com.example.myaku_rismu.feature.profileDetail

import com.example.myaku_rismu.core.UiEvent
import com.example.myaku_rismu.data.model.ProfileSwitchType

sealed interface ProfileDetailUiEvent : UiEvent {
    data class OnClickSwitch(val switchType: ProfileSwitchType) : ProfileDetailUiEvent
    data object OnClickSetting : ProfileDetailUiEvent
}