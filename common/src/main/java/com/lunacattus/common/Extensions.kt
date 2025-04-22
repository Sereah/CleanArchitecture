package com.lunacattus.common

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
* 将日期时间字符串解析为时间戳（Long）
* 支持格式：
* 1. yyyy-MM-dd HH:mm:ss → 转为 LocalDateTime 的时间戳
* 2. yyyy-MM-dd → 转为 LocalDate 的时间戳（默认时间设为 00:00:00）
* 3. HH:mm:ss → 转为当天的时间戳（日期为当前日期）
*/
fun String.parseToTimestamp(): Long? {
    return try {
        when {
            // 格式：yyyy-MM-dd HH:mm:ss
            matches(Regex("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")) -> {
                LocalDateTime
                    .parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            }
            // 格式：yyyy-MM-dd
            matches(Regex("^\\d{4}-\\d{2}-\\d{2}$")) -> {
                LocalDate
                    .parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            }
            // 格式：HH:mm:ss
            matches(Regex("^\\d{2}:\\d{2}:\\d{2}$")) -> {
                LocalTime
                    .parse(this, DateTimeFormatter.ofPattern("HH:mm:ss"))
                    .atDate(LocalDate.now()) // 使用当前日期
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            }
            else -> null // 格式不匹配
        }
    } catch (e: DateTimeParseException) {
        null // 解析失败
    }
}

/**
* 将时间戳（Long）转换为指定格式的日期时间字符串
* @param timestamp 时间戳（毫秒）
* @param format 目标格式（默认 "yyyy-MM-dd HH:mm:ss"）
*/
fun Long.toFormattedDateTime(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    val formatter = DateTimeFormatter.ofPattern(format)
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(formatter)
}