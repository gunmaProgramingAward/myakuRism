package com.example.myaku_rismu.domain.model

import com.example.myaku_rismu.R

data class SettingData(
    val heightCm: Int? = null,
    val weightKg: Int? = null,
    val gender: Gender? = null,
    val birthYear: Int? = null,
    val birthMonth: Int? = null,
    val birthDay: Int? = null,
    val activityLevel: ActivityLevel? = null
)

enum class ActivityLevel(val id: Int, val mainTextRes: Int, val subTextRes: Int) {
    LOW(1, R.string.activity_level_low_main, R.string.activity_level_low_sub),
    MEDIUM(2, R.string.activity_level_medium_main, R.string.activity_level_medium_sub),
    HIGH(3, R.string.activity_level_high_main, R.string.activity_level_high_sub);

    companion object {
        fun fromId(id: Int?): ActivityLevel? {
            return entries.find { it.id == id }
        }
    }
}

enum class Gender(val id: Int, val displayName: Int) {
    MALE(0, R.string.gender_male),
    FEMALE(1, R.string.gender_female),
    OTHER(2, R.string.gender_other);

    companion object {
        fun fromId(id: Int?): Gender? {
            return entries.find { it.id == id }
        }
    }
}