package com.lunacattus.common

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import kotlin.math.roundToInt

/**
 * 将日期时间字符串解析为时间戳（Long）
 */
fun String.parseToTimestamp(zoneId: ZoneId = ZoneId.systemDefault()): Long {
    return try {
        when {
            // 格式：yyyy-MM-dd HH:mm:ss
            matches(Regex("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")) -> {
                LocalDateTime
                    .parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .atZone(zoneId)
                    .toInstant()
                    .toEpochMilli()
            }
            // 格式：yyyy-MM-dd
            matches(Regex("^\\d{4}-\\d{2}-\\d{2}$")) -> {
                LocalDate
                    .parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    .atStartOfDay(zoneId)
                    .toInstant()
                    .toEpochMilli()
            }
            // 格式：HH:mm:ss
            matches(Regex("^\\d{2}:\\d{2}:\\d{2}$")) -> {
                LocalTime
                    .parse(this, DateTimeFormatter.ofPattern("HH:mm:ss"))
                    .atDate(LocalDate.now(zoneId)) // 使用当前日期
                    .atZone(zoneId)
                    .toInstant()
                    .toEpochMilli()
            }
            // 格式：HH:mm
            matches(Regex("^\\d{2}:\\d{2}$")) -> {
                LocalTime
                    .parse(this, DateTimeFormatter.ofPattern("HH:mm"))
                    .atDate(LocalDate.now(zoneId)) // 使用当前日期
                    .atZone(zoneId)
                    .toInstant()
                    .toEpochMilli()
            }
            // 格式：yyyy-MM-ddTHH:mm:ssXXX（带时区偏移量）
            matches(Regex("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}[-+]\\d{2}:\\d{2}$")) -> {
                OffsetDateTime
                    .parse(this, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    .toInstant()
                    .toEpochMilli()
            }
            // 格式：yyyy-MM-ddTHH:mmXXX（带时区偏移量）
            matches(Regex("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}[-+]\\d{2}:\\d{2}$")) -> {
                OffsetDateTime
                    .parse(this, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmXXX"))
                    .toInstant()
                    .toEpochMilli()
            }

            else -> -1L // 格式不匹配
        }
    } catch (_: DateTimeParseException) {
        -1L // 解析失败
    }
}

/**
 * 将时间戳（Long）转换为指定格式的日期时间字符串
 * 如果时间戳是三天内，则只输出小时（HH）
 * @param format 目标格式（默认 "yyyy-MM-dd HH:mm:ss"）
 */
fun Long.toFormattedDateTime(
    format: String = "yyyy-MM-dd HH:mm:ss",
    zoneId: ZoneId = ZoneId.systemDefault()
): String {
    val formatter = DateTimeFormatter.ofPattern(format)
    return Instant.ofEpochMilli(this)
        .atZone(zoneId)
        .format(formatter)
}

/**
 * 将时间戳（Long）转换为 ISO 8601 格式（yyyy-MM-ddTHH:mm:ssXXX）
 */
fun Long.toIso8601DateTime(zoneId: ZoneId = ZoneId.systemDefault()): String {
    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    return Instant.ofEpochMilli(this)
        .atZone(zoneId)
        .format(formatter)
}

/**
 * 将时间戳（Long）转换为星期几的中文表示
 */
fun Long.toChineseDayOfWeek(zoneId: ZoneId = ZoneId.systemDefault()): String {
    val instant = Instant.ofEpochMilli(this)
    val zonedDateTime = instant.atZone(zoneId)
    val dayOfWeek = zonedDateTime.dayOfWeek

    return when (dayOfWeek) {
        DayOfWeek.MONDAY -> "周一"
        DayOfWeek.TUESDAY -> "周二"
        DayOfWeek.WEDNESDAY -> "周三"
        DayOfWeek.THURSDAY -> "周四"
        DayOfWeek.FRIDAY -> "周五"
        DayOfWeek.SATURDAY -> "周六"
        DayOfWeek.SUNDAY -> "周日"
    }
}

/**
 * 根据时间戳判断是否是今天
 */
fun Long.isToday(zoneId: ZoneId = ZoneId.systemDefault()): Boolean {
    val givenDate = Instant.ofEpochMilli(this).atZone(zoneId).toLocalDate()
    val currentDate = LocalDate.now(zoneId)
    return givenDate == currentDate
}

/**
 * 将阿拉伯数字转换为汉字
 */
fun Int.toChineseOrEmpty(): String {
    val chineseNumbers = arrayOf("零", "一", "二", "三", "四", "五", "六", "七", "八", "九")
    return when {
        this in 0..9 -> chineseNumbers[this]
        else -> ""
    }
}

/**
 * 将dp转化为px
 */
fun Int.dpToPx(context: Context): Int =
    (this * context.resources.displayMetrics.density).roundToInt()

/**
 * 输入框输入完文字后的防抖策略
 */
fun EditText.addTextChangeDebounceListener(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    afterTextChanged: (String) -> Unit
) {
    var debounceJob: Job? = null
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            s: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
        }

        override fun afterTextChanged(s: Editable?) {
            debounceJob?.cancel()
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                afterTextChanged(s.toString())
            }
        }
    })
}

/**
 * 点击事件防抖
 */
inline fun View.setOnClickListenerWithDebounce(
    debounceTime: Long = 500,
    crossinline action: (View) -> Unit
) {
    var lastClickTime = 0L
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime >= debounceTime) {
            lastClickTime = currentTime
            action(it)
        }
    }
}