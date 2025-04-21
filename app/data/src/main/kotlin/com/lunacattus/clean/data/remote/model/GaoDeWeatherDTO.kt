package com.lunacattus.clean.data.remote.model

import com.google.gson.annotations.SerializedName

data class GaoDeWeatherDTO(
    val status: Int,
    val count: Int,
    val info: String,
    val infoCode: Int,
    val lives: List<Live>,
)

data class Live(
    val province: String,
    val city: String,
    val weather: String,
    @SerializedName("temperature_float") val temperature: Float,
    @SerializedName("winddirection")val windDirection: String,
    @SerializedName("windpower")val windPower: String,
    val humidity: Int,
    @SerializedName("reporttime")val reportTime: String
)