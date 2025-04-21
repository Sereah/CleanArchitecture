package com.lunacattus.clean.data.remote.model

import com.google.gson.annotations.SerializedName

data class GaoDeIpInfoDTO(
    val status: Int,
    @SerializedName("adcode") val adCode: Any,
)

data class GaoDeWeatherDTO(
    val status: Int,
    val lives: List<Live>,
)

data class Live(
    val province: String,
    val city: String,
    val weather: String,
    @SerializedName("temperature_float") val temperature: Float,
    @SerializedName("winddirection") val windDirection: String,
    @SerializedName("windpower") val windPower: String,
    val humidity: Int,
    @SerializedName("reporttime") val reportTime: String
)