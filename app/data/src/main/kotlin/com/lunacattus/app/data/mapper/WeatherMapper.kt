package com.lunacattus.app.data.mapper

import com.lunacattus.app.data.local.entity.DailyWeatherEntity
import com.lunacattus.app.data.local.entity.WeatherEntity
import com.lunacattus.app.data.local.entity.WeatherWithDailyWeather
import com.lunacattus.app.data.remote.dto.GaoDeDailyWeatherDTO
import com.lunacattus.app.data.remote.dto.GaoDeLiveWeatherDTO
import com.lunacattus.app.data.remote.dto.GaoDeSearchCityDTO
import com.lunacattus.app.domain.model.CityInfo
import com.lunacattus.app.domain.model.DailyForecast
import com.lunacattus.app.domain.model.WeatherCondition
import com.lunacattus.app.domain.model.WeatherInfo
import com.lunacattus.app.domain.model.WindDirection
import com.lunacattus.common.parseToTimestamp

object WeatherMapper {

    fun GaoDeLiveWeatherDTO.mapperToEntity(): WeatherEntity {
        return WeatherEntity(
            adCode = this.lives[0].adCode.toInt(),
            provence = this.lives[0].province,
            city = this.lives[0].city,
            condition = this.lives[0].weather,
            temperature = this.lives[0].temperature,
            windDirection = this.lives[0].windDirection,
            windPower = this.lives[0].windPower,
            humidity = this.lives[0].humidity,
            reportTime = this.lives[0].reportTime,
            updateTime = System.currentTimeMillis()
        )
    }

    fun GaoDeDailyWeatherDTO.mapperToEntity(): List<DailyWeatherEntity> {
        return this.forecasts.flatMap { forecast ->
            forecast.casts.map { cast ->
                DailyWeatherEntity(
                    adCode = forecast.adCode.toInt(),
                    date = cast.date,
                    week = cast.week,
                    dayCondition = cast.dayCondition,
                    nightCondition = cast.nightCondition,
                    dayTemp = cast.dayTemp,
                    nightTemp = cast.nightTemp,
                    dayWindDirection = cast.dayWindDirection,
                    nightWindDirection = cast.nightWindDirection,
                    dayWindPower = cast.dayWindPower,
                    nightWindPower = cast.nightWindPower,
                    reportTime = forecast.reportTime,
                    updateTime = System.currentTimeMillis()
                )
            }
        }
    }

    fun GaoDeSearchCityDTO.mapperToModel(): List<CityInfo> {
        return this.poiList.map {
            CityInfo(
                it.adName,
                it.pName,
                it.adCode
            )
        }
    }

    fun WeatherWithDailyWeather.mapperToModel(): WeatherInfo {
        return WeatherInfo(
            adCode = this.weather.adCode,
            provence = this.weather.provence,
            city = this.weather.city,
            condition = mapperCondition(this.weather.condition),
            temperature = this.weather.temperature,
            windDirection = mapperWindDirection(this.weather.windDirection),
            windPower = this.weather.windPower,
            humidity = this.weather.humidity,
            updateTime = this.weather.updateTime,
            dailyForecast = mapperDailyForecast(this.dailyWeather)
        )
    }

    private fun mapperDailyForecast(forecasts: List<DailyWeatherEntity>): List<DailyForecast> {
        return forecasts.map {
            DailyForecast(
                date = it.date.parseToTimestamp() ?: -1L,
                week = it.week,
                minTemp = minOf(it.dayTemp, it.nightTemp),
                maxTemp = maxOf(it.dayTemp, it.nightTemp),
                condition = mapperCondition(it.dayCondition, it.nightCondition)
            )
        }
    }

    private fun mapperCondition(vararg conditions: String): WeatherCondition {
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

    private fun mapperWindDirection(vararg directions: String): WindDirection {
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