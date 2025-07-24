package com.example.myaku_rismu.domain.model

import com.example.myaku_rismu.R

data class SettingData(
    val heightCm: Int? = null,
    val weightKg: Int? = null,
    val gender: Int? = null,
    val birthYear: Int? = null,
    val birthMonth: Int? = null,
    val birthDay: Int? = null,
    val activityLevel: ActivityLevel = ActivityLevel.MEDIUM,
)

enum class ActivityLevel(val id: Int, val mainTextRes: Int, val subTextRes: Int) {
    LOW(1, R.string.activity_level_low_main, R.string.activity_level_low_sub),
    MEDIUM(2, R.string.activity_level_medium_main, R.string.activity_level_medium_sub),
    HIGH(3, R.string.activity_level_high_main, R.string.activity_level_high_sub);

    companion object {
        fun fromId(id: Int): ActivityLevel = entries.find { it.id == id } ?: MEDIUM
    }
}