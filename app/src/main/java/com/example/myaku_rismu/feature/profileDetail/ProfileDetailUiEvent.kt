package com.example.myaku_rismu.feature.profileDetail

import com.example.myaku_rismu.core.UiEvent

sealed interface ProfileDetailUiEvent : UiEvent {
    data class OnClickSwitch(val switchType: Int) : ProfileDetailUiEvent
    object OnClickSetting : ProfileDetailUiEvent
}