package com.lunacattus.app.data.mapper

import com.lunacattus.app.data.local.entity.GaoDeDailyWeatherEntity
import com.lunacattus.app.data.local.entity.QWeatherDailyEntity
import com.lunacattus.app.data.local.entity.QWeatherHourlyEntity
import com.lunacattus.app.data.local.entity.QWeatherLocationEntity
import com.lunacattus.app.data.local.entity.QWeatherNowEntity
import com.lunacattus.app.data.local.entity.GaoDeLiveWeatherEntity
import com.lunacattus.app.data.local.entity.GaoDeWeatherWithDailyWeather
import com.lunacattus.app.data.remote.dto.GaoDeDailyWeatherDTO
import com.lunacattus.app.data.remote.dto.GaoDeLiveWeatherDTO
import com.lunacattus.app.data.remote.dto.GaoDeSearchCityDTO
import com.lunacattus.app.data.remote.dto.QWeatherDailyDTO
import com.lunacattus.app.data.remote.dto.QWeatherGeoDTO
import com.lunacattus.app.data.remote.dto.QWeatherHourlyDTO
import com.lunacattus.app.data.remote.dto.QWeatherNowDTO
import com.lunacattus.app.domain.model.CityInfo
import com.lunacattus.app.domain.model.DailyForecast
import com.lunacattus.app.domain.model.WeatherCondition
import com.lunacattus.app.domain.model.WeatherInfo
import com.lunacattus.app.domain.model.WindDirection
import com.lunacattus.common.parseToTimestamp

object WeatherMapper {

    fun QWeatherGeoDTO.mapperToEntity(): List<QWeatherLocationEntity> {
        return this.location.map {
            QWeatherLocationEntity(
                locationId = it.id,
                name = it.name,
                lat = it.lat,
                lon = it.lon,
                country = it.country,
                province = it.provence,
                city = it.city,
                timeZone = it.timeZone
            )
        }
    }

    fun QWeatherNowDTO.mapperToEntity(locationId: String): QWeatherNowEntity {
        return QWeatherNowEntity(
            locationId = locationId,
            obsTime = this.now.obsTime,
            temp = this.now.temp,
            feelsTemp = this.now.feelsTemp,
            text = this.now.text,
            wind360 = this.now.wind360,
            windScale = this.now.windScale,
            windSpeed = this.now.windSpeed,
            humidity = this.now.humidity,
            preCip = this.now.preCip,
            pressure = this.now.pressure,
            vis = this.now.vis,
            cloud = this.now.cloud,
            dew = this.now.dew
        )
    }

    fun QWeatherDailyDTO.mapperToEntity(locationId: String): List<QWeatherDailyEntity> {
        return this.daily.map {
            QWeatherDailyEntity(
                locationId = locationId,
                fxDate = it.fxDate,
                sunrise = it.sunrise,
                sunset = it.sunset,
                moonrise = it.moonrise,
                moonSet = it.moonSet,
                moonPhase = it.moonPhase,
                tempMax = it.tempMax,
                tempMin = it.tempMin,
                textDay = it.textDay,
                textNight = it.textNight,
                wind360Day = it.wind360Day,
                windScaleDay = it.windScaleDay,
                windSpeedDay = it.windSpeedDay,
                wind360Night = it.wind360Night,
                windScaleNight = it.windScaleNight,
                windSpeedNight = it.windSpeedNight,
                humidity = it.humidity,
                preCip = it.preCip,
                pressure = it.pressure,
                vis = it.vis,
                cloud = it.cloud,
                uvIndex = it.uvIndex
            )
        }
    }

    fun QWeatherHourlyDTO.mapperToEntity(locationId: String): List<QWeatherHourlyEntity> {
        return this.hourly.map {
            QWeatherHourlyEntity(
                locationId = locationId,
                fxTime = it.fxTime,
                temp = it.temp,
                text = it.text,
                wind360 = it.wind360,
                windScale = it.windScale,
                windSpeed = it.windSpeed,
                humidity = it.humidity,
                pop = it.pop,
                preCip = it.preCip,
                pressure = it.pressure,
                cloud = it.cloud,
                dew = it.dew
            )
        }
    }

    fun GaoDeLiveWeatherDTO.mapperToEntity(): GaoDeLiveWeatherEntity {
        return GaoDeLiveWeatherEntity(
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

    fun GaoDeDailyWeatherDTO.mapperToEntity(): List<GaoDeDailyWeatherEntity> {
        return this.forecasts.flatMap { forecast ->
            forecast.casts.map { cast ->
                GaoDeDailyWeatherEntity(
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

    fun GaoDeWeatherWithDailyWeather.mapperToModel(): WeatherInfo {
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

    private fun mapperDailyForecast(forecasts: List<GaoDeDailyWeatherEntity>): List<DailyForecast> {
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
                "旋转不定" -> WindDirection.ROTATIONAL
                else -> WindDirection.NONE
            }
        }
        return WindDirection.NONE
    }
}