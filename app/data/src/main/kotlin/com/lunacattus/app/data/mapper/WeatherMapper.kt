package com.lunacattus.app.data.mapper

import com.lunacattus.app.data.local.entity.GaoDeDailyWeatherEntity
import com.lunacattus.app.data.local.entity.GaoDeLiveWeatherEntity
import com.lunacattus.app.data.local.entity.QWeatherDailyEntity
import com.lunacattus.app.data.local.entity.QWeatherEntity
import com.lunacattus.app.data.local.entity.QWeatherGeoEntity
import com.lunacattus.app.data.local.entity.QWeatherHourlyEntity
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

    fun QWeatherGeoDTO.mapperToEntity(isCurrentLocation: Boolean): List<QWeatherGeoEntity> {
        return this.location.map {
            QWeatherGeoEntity(
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

    fun QWeatherGeoDTO.mapperToModel(isCurrentLocation: Boolean): List<WeatherGeo> {
        return this.location.map {
            WeatherGeo(
                name = it.name,
                id = it.id,
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

    fun QWeatherNowDTO.mapperToEntity(
        locationId: String,
        isCurrentLocation: Boolean
    ): QWeatherNowEntity {
        return QWeatherNowEntity(
            locationId = locationId,
            isCurrentLocation = isCurrentLocation,
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

    fun QWeatherNowDTO.mapperToModel(
        locationId: String,
        isCurrentLocation: Boolean
    ): NowWeather {
        return NowWeather(
            id = locationId,
            isCurrentLocation = isCurrentLocation,
            obsTime = this.now.obsTime.parseToTimestamp(),
            temp = this.now.temp,
            feelsLike = this.now.feelsTemp,
            weatherText = WeatherText.fromString(this.now.text),
            windDirection = WindDirection.fromDegrees(this.now.wind360),
            windScale = WindScale.fromInput(this.now.windScale),
            windSpeed = this.now.windSpeed,
            humidity = this.now.humidity,
            preCip = this.now.preCip,
            pressure = this.now.pressure,
            vis = this.now.vis,
            cloud = this.now.cloud,
            dew = this.now.dew
        )
    }

    fun QWeatherDailyDTO.mapperToEntity(
        locationId: String,
        isCurrentLocation: Boolean
    ): List<QWeatherDailyEntity> {
        return this.daily.map {
            QWeatherDailyEntity(
                locationId = locationId,
                isCurrentLocation = isCurrentLocation,
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

    fun QWeatherDailyDTO.mapperToModel(
        locationId: String,
        isCurrentLocation: Boolean
    ): List<DailyWeather> {
        return this.daily.map {
            DailyWeather(
                id = locationId,
                isCurrentLocation = isCurrentLocation,
                date = it.fxDate.parseToTimestamp(),
                sunrise = it.sunrise.parseToTimestamp(),
                sunset = it.sunset.parseToTimestamp(),
                moonrise = it.moonrise.parseToTimestamp(),
                moonSet = it.moonSet.parseToTimestamp(),
                moonPhase = it.moonPhase,
                tempMax = it.tempMax,
                tempMin = it.tempMin,
                dayWeatherText = WeatherText.fromString(it.textDay),
                nightWeatherText = WeatherText.fromString(it.textNight),
                dayWindDirect = WindDirection.fromDegrees(it.wind360Day),
                dayWindScale = WindScale.fromInput(it.windScaleDay),
                dayWindSpeed = it.windSpeedDay,
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
    }

    fun QWeatherHourlyDTO.mapperToEntity(
        locationId: String,
        isCurrentLocation: Boolean
    ): List<QWeatherHourlyEntity> {
        return this.hourly.map {
            QWeatherHourlyEntity(
                locationId = locationId,
                isCurrentLocation = isCurrentLocation,
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

    fun QWeatherHourlyDTO.mapperToModel(
        locationId: String,
        isCurrentLocation: Boolean
    ): List<HourlyWeather> {
        return this.hourly.map {
            HourlyWeather(
                id = locationId,
                isCurrentLocation = isCurrentLocation,
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
    }

    fun QWeatherGeoEntity.mapperToModel(): WeatherGeo {
        return WeatherGeo(
            name = this.name,
            id = this.locationId,
            lat = this.lat,
            lon = this.lon,
            country = this.country,
            province = this.province,
            city = this.city,
            timeZone = this.timeZone,
            isCurrentLocation = this.isCurrentLocation
        )
    }

    fun QWeatherNowEntity.mapperToModel(): NowWeather {
        return NowWeather(
            id = this.locationId,
            obsTime = this.obsTime.parseToTimestamp(),
            temp = this.temp,
            feelsLike = this.feelsTemp,
            weatherText = WeatherText.fromString(this.text),
            windDirection = WindDirection.fromDegrees(this.wind360),
            windScale = WindScale.fromInput(this.windScale),
            windSpeed = this.windSpeed,
            humidity = this.humidity,
            preCip = this.preCip,
            pressure = this.pressure,
            vis = this.vis,
            cloud = this.cloud,
            dew = this.dew,
            isCurrentLocation = this.isCurrentLocation
        )
    }

    fun QWeatherDailyEntity.mapperToModel(): DailyWeather {
        return this.let {
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
                uvIndex = it.uvIndex,
                isCurrentLocation = it.isCurrentLocation
            )
        }
    }

    fun QWeatherHourlyEntity.mapperToModel(): HourlyWeather {
        return this.let {
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
                dew = it.dew,
                isCurrentLocation = it.isCurrentLocation
            )
        }
    }

    fun QWeatherEntity.mapperToModel(): Weather {
        val geo = geo.mapperToModel()
        val nowWeather = now?.mapperToModel() ?: NowWeather()
        val dailyWeather = daily.map { it.mapperToModel() }
        val hourlyWeather = hourly.map { it.mapperToModel() }
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