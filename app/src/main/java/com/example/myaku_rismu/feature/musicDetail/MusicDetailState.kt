package com.example.myaku_rismu.feature.musicDetail

import com.example.myaku_rismu.core.ScreenState
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class MusicDetailState(
    val screenState: ScreenState = ScreenState.Initializing()
) {

    val monthlyGraphTitle: String
        get() {
            val current = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy年M月")
            return current.format(formatter)
        }

    val yearlyGraphTitle: String
        get() {
            val current = LocalDate.now()
            return "${current.year}年"
        }
}


data class AxisConfig(
    val maxValue: Int,
    val labelFormatter: (Int) -> String,
    val spacing: Int,
    val totalLabels: Int
)

