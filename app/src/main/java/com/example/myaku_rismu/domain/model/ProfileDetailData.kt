package com.example.myaku_rismu.domain.model

data class ProfileDetailData(
    val includeLyricsSwitchEnabled: Boolean = false,
    val musicGenerationNotificationSwitchEnabled: Boolean = false,
    val collaborationWithHealthcareSwitchEnabled: Boolean = false,
    val syncWithYourSmartwatchSwitchEnabled: Boolean = false,
)
