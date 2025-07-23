package com.example.myaku_rismu.domain.model

import java.time.Instant

data class HealthDataCacheEntry(
    val data: List<Long>,
    val timestamp: Instant,
    val ttl: Long = 300_000L // 5分間のTTL
) {
    fun isExpired(): Boolean = Instant.now().toEpochMilli() - timestamp.toEpochMilli() > ttl
}
