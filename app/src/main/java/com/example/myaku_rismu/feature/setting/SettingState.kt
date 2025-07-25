package com.example.myaku_rismu.feature.setting

import com.example.myaku_rismu.core.ScreenState
import com.example.myaku_rismu.data.model.SettingType
import com.example.myaku_rismu.domain.model.SettingData


data class SettingState(
    val screenState: ScreenState = ScreenState.Initializing(),
    val display: SettingData = SettingData(),
    val dialog: SettingType? = null,
    val isHealthConnectLinked: Boolean = false,
)
