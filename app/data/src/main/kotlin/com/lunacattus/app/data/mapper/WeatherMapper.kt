package com.lunacattus.app.data.mapper

import com.lunacattus.app.data.local.entity.GaoDeDailyWeatherEntity
import com.lunacattus.app.data.local.entity.GaoDeLiveWeatherEntity
import com.lunacattus.app.data.local.entity.QWeatherCombinedData
import com.lunacattus.app.data.local.entity.QWeatherDailyEntity
import com.lunacattus.app.data.local.entity.QWeatherHourlyEntity
import com.lunacattus.app.data.local.entity.QWeatherLocationEntity
import com.lunacattus.app.data.local.entity.QWeatherNowEntity
import com.lunacattus.app.data.remote.dto.GaoDeDailyWeatherDTO
import com.lunacattus.app.data.remote.dto.GaoDeLiveWeatherDTO
import com.lunacattus.app.data.remote.dto.QWeatherDailyDTO
import com.lunacattus.app.data.remote.dto.QWeatherGeoDTO
import com.lunacattus.app.data.remote.dto.QWeatherHourlyDTO
import com.lunacattus.app.data.remote.dto.QWeatherNowDTO
import com.lunacattus.app.domain.model.DailyWeather
import com.lunacattus.app.domain.model.HourlyWeather
import com.lunacattus.app.domain.model.NowWeather
import com.lunacattus.app.domain.model.Weather
import com.lunacattus.app.domain.model.WeatherGeo
import com.lunacattus.app.domain.model.WeatherText
import com.lunacattus.app.domain.model.WindDirection
import com.lunacattus.app.domain.model.WindScale
import com.lunacattus.common.parseToTimestamp

object WeatherMapper {

    fun QWeatherGeoDTO.mapperToEntity(isCurrentLocation: Boolean): QWeatherLocationEntity {
        return this.location[0].let {
            QWeatherLocationEntity(
                locationId = it.id,
                name = it.name,
                lat = it.lat,
                lon = it.lon,
                country = it.country,
                province = it.provence,
                city = it.city,
                timeZone = it.timeZone,
                isCurrentLocation = isCurrentLocation
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

    fun QWeatherCombinedData.mapperToModel(): Weather {
        val geo = WeatherGeo(
            name = location.name,
            id = location.locationId,
            lat = location.lat,
            lon = location.lon,
            country = location.country,
            province = location.province,
            city = location.city,
            timeZone = location.timeZone
        )
        val nowWeather = now?.let { now ->
            NowWeather(
                id = now.locationId,
                obsTime = now.obsTime.parseToTimestamp(),
                temp = now.temp,
                feelsLike = now.feelsTemp,
                weatherText = WeatherText.fromString(now.text),
                windDirection = WindDirection.fromDegrees(now.wind360),
                windScale = WindScale.fromInput(now.windScale),
                windSpeed = now.windSpeed,
                humidity = now.humidity,
                preCip = now.preCip,
                pressure = now.pressure,
                vis = now.vis,
                cloud = now.cloud,
                dew = now.dew
            )
        } ?: NowWeather()
        val dailyWeather = daily.map {
            DailyWeather(
                id = it.locationId,
                date = it.fxDate.parseToTimestamp(),
                sunrise = it.sunrise.parseToTimestamp(),
                sunset = it.sunset.parseToTimestamp(),
                moonrise = it.moonrise.parseToTimestamp(),
                moonSet = it.moonSet.parseToTimestamp(),
                moonPhase = it.moonPhase,
                tempMax = it.tempMax,
                tempMin = it.tempMin,
                dayWeatherText = WeatherText.fromString(it.textDay),
                dayWindDirect = WindDirection.fromDegrees(it.wind360Day),
                dayWindScale = WindScale.fromInput(it.windScaleDay),
                dayWindSpeed = it.windSpeedDay,
                nightWeatherText = WeatherText.fromString(it.textNight),
                nightWindDirect = WindDirection.fromDegrees(it.wind360Night),
                nightWindScale = WindScale.fromInput(it.windScaleNight),
                nightWindSpeed = it.windSpeedNight,
                humidity = it.humidity,
                preCip = it.preCip,
                pressure = it.pressure,
                vis = it.vis,
                cloud = it.cloud,
                uvIndex = it.uvIndex
            )
        }
        val hourlyWeather = hourly.map {
            HourlyWeather(
                id = it.locationId,
                time = it.fxTime.parseToTimestamp(),
                temp = it.temp,
                weatherText = WeatherText.fromString(it.text),
                windDirection = WindDirection.fromDegrees(it.wind360),
                windScale = WindScale.fromInput(it.windScale),
                windSpeed = it.windSpeed,
                humidity = it.humidity,
                pop = it.pop,
                preCip = it.preCip,
                pressure = it.pressure,
                cloud = it.cloud,
                dew = it.dew
            )
        }
        return Weather(geo, nowWeather, dailyWeather, hourlyWeather)
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
}