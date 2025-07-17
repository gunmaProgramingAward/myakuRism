package com.example.myaku_rismu.feature.profileDetail

import com.example.myaku_rismu.core.ScreenState

data class ProfileDetailState(
    val screenState: ScreenState = ScreenState.Initializing(),
    val includeLyricsSwitchEnabled: Boolean = false,
    val musicGenerationNotificationSwitchEnabled: Boolean = false,
    val collaborationWithHealthcareSwitchEnabled: Boolean = false,
    val syncWithYourSmartwatchSwitchEnabled: Boolean = false
)
