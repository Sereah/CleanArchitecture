package com.lunacattus.app.data.mapper

import com.lunacattus.common.parseToTimestamp
import com.lunacattus.app.data.remote.dto.Forecast
import com.lunacattus.app.data.remote.dto.GaoDeDailyWeatherDTO
import com.lunacattus.app.data.remote.dto.GaoDeLiveWeatherDTO
import com.lunacattus.app.domain.model.weather.DailyForecast
import com.lunacattus.app.domain.model.weather.WeatherCondition
import com.lunacattus.app.domain.model.weather.WeatherInfo
import com.lunacattus.app.domain.model.weather.WindDirection

object GaoDeWeatherMapper {

    fun mapper(live: GaoDeLiveWeatherDTO, daily: GaoDeDailyWeatherDTO?): WeatherInfo {
        return WeatherInfo(
            provence = live.lives[0].province,
            city = live.lives[0].city,
            condition = mapperCondition(live.lives[0].weather),
            temperature = live.lives[0].temperature,
            windDirection = mapperWindDirection(live.lives[0].windDirection),
            windPower = live.lives[0].windPower,
            humidity = live.lives[0].humidity,
            dailyForecast = daily?.let { mapperDailyForecast(it.forecasts) } ?: emptyList(),
            reportTime = (live.lives[0].reportTime).parseToTimestamp() ?: -1L
        )
    }

    fun mapperDailyForecast(forecasts: List<Forecast>): List<DailyForecast> {
        return forecasts.flatMap { forecast ->
            forecast.casts.map { cast ->
                val minTemp = minOf(cast.dayTemp, cast.nightTemp)
                val maxTemp = maxOf(cast.dayTemp, cast.nightTemp)
                val condition = mapperCondition(cast.dayCondition, cast.nightCondition)
                DailyForecast(
                    date = cast.date.parseToTimestamp() ?: -1L,
                    minTemp = minTemp,
                    maxTemp = maxTemp,
                    condition = condition
                )
            }
        }
    }

    fun mapperCondition(vararg conditions: String): WeatherCondition {
        for (condition in conditions) {
            return when {
                condition.contains("晴") -> WeatherCondition.SUNNY
                condition.contains("多云") -> WeatherCondition.CLOUDY
                condition.contains("雨") -> WeatherCondition.RAINY
                condition.contains("雪") -> WeatherCondition.SNOWY
                condition.contains("雷") -> WeatherCondition.THUNDERSTORM
                condition.contains("雾") -> WeatherCondition.FOGGY
                condition.contains("风") -> WeatherCondition.WINDY
                else -> WeatherCondition.UNKNOWN
            }
        }
        return WeatherCondition.UNKNOWN
    }

    fun mapperWindDirection(vararg directions: String): WindDirection {
        for (direction in directions) {
            return when (direction) {
                "东" -> WindDirection.EAST
                "南" -> WindDirection.SOUTH
                "西" -> WindDirection.WEST
                "北" -> WindDirection.NORTH
                "东南" -> WindDirection.SOUTHEAST
                "东北" -> WindDirection.NORTHEAST
                "西南" -> WindDirection.SOUTHWEST
                "西北" -> WindDirection.NORTHWEST
                "旋转不定" -> WindDirection.INSTABILITY
                else -> WindDirection.NONE
            }
        }
        return WindDirection.NONE
    }
}