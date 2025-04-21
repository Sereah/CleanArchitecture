package com.lunacattus.clean.data.mapper

import com.lunacattus.clean.data.remote.model.GaoDeWeatherDTO
import com.lunacattus.clean.domain.model.weather.LivesWeather

object WeatherMapper {
    fun GaoDeWeatherDTO.mapper(): LivesWeather {
        return LivesWeather(
            provence = this.lives[0].province,
            city = this.lives[0].city,
            weather = this.lives[0].weather,
            temperature = this.lives[0].temperature,
            windDirection = this.lives[0].windDirection,
            windPower = this.lives[0].windPower,
            humidity = this.lives[0].humidity,
            reportTime = this.lives[0].reportTime
        )
    }
}