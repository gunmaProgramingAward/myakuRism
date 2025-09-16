package com.example.myaku_rismu.feature.profileDetail

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.myaku_rismu.core.ScreenState

data class ProfileDetailState(
    val screenState: ScreenState = ScreenState.Initializing,
    val display: ProfileDetailData = ProfileDetailData()
)

data class ProfileDetailData(
    val includeLyricsSwitchEnabled: Boolean = false,
    val musicGenerationNotificationSwitchEnabled: Boolean = false,
    val collaborationWithHealthcareSwitchEnabled: Boolean = false,
    val syncWithYourSmartwatchSwitchEnabled: Boolean = false,
)

data class CardItem(
    @DrawableRes val iconResId: Int,
    @StringRes val title: Int,
    val isSwitchEnabled: Boolean
)

