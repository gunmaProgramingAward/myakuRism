package com.example.myaku_rismu.domain.model

sealed class SunoGenerateRequestAppModel {
    abstract val modelName: ModelName
    abstract val instrumental: Boolean
    abstract val negativeTags: String?

    data class Custom(
        override val modelName: ModelName,
        override val instrumental: Boolean,
        override val negativeTags: String?,
        val prompt: String,
        val style: String,
        val title: String
    ) : SunoGenerateRequestAppModel()

    data class Normal(
        override val modelName: ModelName,
        override val instrumental: Boolean,
        override val negativeTags: String?,
        val prompt: String,
        ) : SunoGenerateRequestAppModel()

    data class Instrumental(
        override val modelName: ModelName,
        override val instrumental: Boolean,
        override val negativeTags: String?,
        val style: String,
        ) : SunoGenerateRequestAppModel()
}

enum class ModelName(val model: String) {
    V3_5("V3_5"),
    V4("V4"),
    V4_5("V4_5"),
    V4_5PLUS("V4_5PLUS"),
}

