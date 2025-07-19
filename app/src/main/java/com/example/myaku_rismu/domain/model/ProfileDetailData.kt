package com.example.myaku_rismu.domain.model

data class ProfileDetailData(
    val includeLyricsSwitchEnabled: Boolean,
    val musicGenerationNotificationSwitchEnabled: Boolean,
    val collaborationWithHealthcareSwitchEnabled: Boolean,
    val syncWithYourSmartwatchSwitchEnabled: Boolean
)
