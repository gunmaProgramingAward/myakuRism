package com.example.myaku_rismu.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music_generation")
data class MusicGenerationEntity(
    @PrimaryKey
    val targetId: String,
    val createdAt: Long = System.currentTimeMillis()
) {
    companion object {
        fun create(targetId: String): MusicGenerationEntity {
            return MusicGenerationEntity(
                targetId = targetId
            )
        }
    }
}