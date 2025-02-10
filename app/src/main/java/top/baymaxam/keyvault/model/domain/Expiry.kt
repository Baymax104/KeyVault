package top.baymaxam.keyvault.model.domain

import kotlin.time.Duration
import kotlin.time.Duration.Companion.INFINITE
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

/**
 * ExpiryDuration
 * @author John
 * @since 08 2月 2025
 */
enum class Expiry(val label: String, val value: Duration) {
    ThirtyMinutes("30分钟", 30.minutes),
    OneHour("1小时", 1.hours),
    OneDay("1天", 1.days),
    FifteenDays("15天", 15.days),
    ThirtyDays("30天", 30.days),
    Forever("永不", INFINITE)
}
