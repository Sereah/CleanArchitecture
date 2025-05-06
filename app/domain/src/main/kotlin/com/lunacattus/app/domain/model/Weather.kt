package com.lunacattus.app.domain.model

import com.lunacattus.app.domain.model.WeatherText.entries
import com.lunacattus.app.domain.model.WindDirection.entries
import com.lunacattus.app.domain.model.WindScale.entries

data class Weather(
    val geo: WeatherGeo,
    val nowWeather: NowWeather,
    val dailyWeather: List<DailyWeather>,
    val hourlyWeather: List<HourlyWeather>,
)

data class WeatherGeo(
    val name: String,
    val id: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val province: String,
    val city: String,
    val timeZone: String,
    val isCurrentLocation: Boolean,
)

data class NowWeather(
    val id: String = "",
    val obsTime: Long = 0L,
    val temp: Int = 0,
    val feelsLike: Int = 0,
    val weatherText: WeatherText = WeatherText.SUNNY,
    val windDirection: WindDirection = WindDirection.NONE,
    val windScale: WindScale = WindScale.GALE,
    val windSpeed: Int = 0, //公里每小时
    val humidity: Int = 0,
    val preCip: Float = 0F, //过去1小时降水量，默认单位：毫米
    val pressure: Int = 0, //大气压强，默认单位：百帕
    val vis: Int = 0, //能见度，默认单位：公里
    val cloud: Int = 0, //云量，百分比数值。可能为空
    val dew: Int = 0, //露点温度。可能为空
    val isCurrentLocation: Boolean = false,
)

data class DailyWeather(
    val id: String,
    val date: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long,
    val moonSet: Long,
    val moonPhase: String, //月相名称
    val tempMax: Int,
    val tempMin: Int,
    val dayWeatherText: WeatherText,
    val dayWindDirect: WindDirection,
    val dayWindScale: WindScale,
    val dayWindSpeed: Int,
    val nightWeatherText: WeatherText,
    val nightWindDirect: WindDirection,
    val nightWindScale: WindScale,
    val nightWindSpeed: Int,
    val humidity: Int,
    val preCip: Float, //预报当天总降水量，默认单位：毫米
    val uvIndex: Int, //紫外线强度
    val pressure: Int, //大气压强，默认单位：百帕
    val vis: Int, //能见度，默认单位：公里
    val cloud: Int, //云量，百分比数值。可能为空
    val isCurrentLocation: Boolean,
)

data class HourlyWeather(
    val id: String,
    val time: Long,
    val temp: Int,
    val weatherText: WeatherText,
    val windDirection: WindDirection,
    val windScale: WindScale,
    val windSpeed: Int,
    val humidity: Int,
    val pop: Int, //逐小时预报降水概率，百分比数值，可能为空
    val preCip: Float, //当前小时累计降水量，默认单位：毫米
    val pressure: Int, //大气压强，默认单位：百帕
    val cloud: Int, //云量，百分比数值。可能为空
    val dew: Int, //露点温度。可能为空
    val isCurrentLocation: Boolean,
)

enum class WeatherText(
    defaultText: String,
    private val matches: List<String>
) {
    SUNNY("晴", listOf("晴", "阳光")),
    CLOUDY("多云", listOf("多云", "阴")),
    RAINY("雨", listOf("雨", "小雨", "中雨", "大雨")),
    STORMY("暴雨", listOf("暴雨", "大暴雨", "特大暴雨")),
    SNOWY("雪", listOf("雪", "小雪", "中雪", "大雪", "暴雪")),
    THUNDERSTORM("雷雨", listOf("雷阵雨", "雷雨", "雷暴")),
    FOGGY("雾", listOf("雾", "大雾", "浓雾")),
    WINDY("风", listOf("风", "大风", "狂风")),
    UNKNOWN("未知", emptyList());

    private var originalText: String = defaultText

    companion object {
        fun fromString(text: String): WeatherText {
            return entries.firstOrNull { condition ->
                condition.matches.any { keyword ->
                    text.contains(keyword)
                }
            }?.apply {
                originalText = text
            } ?: UNKNOWN.apply {
                originalText = text
            }
        }
    }

    override fun toString(): String = originalText
}

enum class WindDirection(
    private val degrees: Float,
    private val minDegree: Double? = null,
    private val maxDegree: Double? = null
) {
    NORTH(0f, 337.5, 22.5),
    NORTHEAST(45f, 22.5, 67.5),
    EAST(90f, 67.5, 112.5),
    SOUTHEAST(135f, 112.5, 157.5),
    SOUTH(180f, 157.5, 202.5),
    SOUTHWEST(225f, 202.5, 247.5),
    WEST(270f, 247.5, 292.5),
    NORTHWEST(315f, 292.5, 337.5),
    ROTATIONAL(-999f),
    NONE(-1f);

    companion object {
        fun fromDegrees(degrees: Float): WindDirection {
            return when (degrees) {
                -999f -> ROTATIONAL
                -1f -> NONE
                else -> {
                    val normalized = (degrees % 360 + 360) % 360
                    entries.firstOrNull { it.contains(normalized) } ?: NONE
                }
            }
        }
    }

    private fun contains(degree: Float): Boolean {
        return when (this) {
            NORTH -> degree >= 337.5 || degree < 22.5
            else -> minDegree?.let { min ->
                maxDegree?.let { max ->
                    degree >= min && degree <= max
                }
            } == true
        }
    }

    override fun toString(): String {
        return when (this) {
            NORTH -> "北风"
            NORTHEAST -> "东北风"
            EAST -> "东风"
            SOUTHEAST -> "东南风"
            SOUTH -> "南风"
            SOUTHWEST -> "西南风"
            WEST -> "西风"
            NORTHWEST -> "西北风"
            ROTATIONAL -> "旋转风"
            NONE -> "无持续风向"
        }
    }
}

enum class WindScale(
    private val levelRange: IntRange,
    private val chineseTerm: String
) {
    CALM(0..0, "无风"),
    LIGHT_AIR(1..1, "软风"),
    LIGHT_BREEZE(2..2, "轻风"),
    GENTLE_BREEZE(3..3, "微风"),
    MODERATE_BREEZE(4..4, "和风"),
    FRESH_BREEZE(5..5, "清风"),
    STRONG_BREEZE(6..6, "强风"),
    NEAR_GALE(7..7, "疾风"),
    GALE(8..8, "大风"),
    STRONG_GALE(9..9, "烈风"),
    STORM(10..10, "狂风"),
    VIOLENT_STORM(11..11, "暴风"),
    HURRICANE(12..12, "飓风");

    var originalInput: String = levelRange.first.toString()
        private set

    companion object {
        fun fromInput(input: String): WindScale {
            return try {
                when {
                    input.contains("-") -> {
                        val (start, end) = input.split("-").map { it.toInt() }
                        entries.firstOrNull { start in it.levelRange || end in it.levelRange }
                            ?.apply { originalInput = input } ?: CALM.apply {
                            originalInput = input
                        }
                    }

                    else -> {
                        entries.firstOrNull { input.toInt() in it.levelRange }
                            ?.apply { originalInput = input } ?: CALM.apply {
                            originalInput = input
                        }
                    }
                }
            } catch (_: Exception) {
                CALM.apply { originalInput = input }
            }
        }
    }

    override fun toString(): String = chineseTerm
}