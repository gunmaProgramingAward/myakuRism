package com.example.myaku_rismu.feature.profileDetail

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.domain.model.ProfileDetailData

data class ProfileDetailState(
    val screenState: ScreenState = ScreenState.Initializing(),
    val display: ProfileDetailData = ProfileDetailData()
)

data class CardItem(
    @DrawableRes val iconResId: Int,
    @StringRes val title: Int,
    val isSwitchEnabled: Boolean
)