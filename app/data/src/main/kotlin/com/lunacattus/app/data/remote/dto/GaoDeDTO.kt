package com.lunacattus.app.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GaoDeLocationInfoDTO(
    val status: Int,
    val info: String,
    @SerializedName("infocode") val infoCode: Int,
    @SerializedName("adcode") val adCode: Any,
    val rectangle: Any,
)

data class GaoDeLiveWeatherDTO(
    val status: Int,
    val info: String,
    @SerializedName("infocode") val infoCode: Int,
    val lives: List<Live>,
)

data class Live(
    val province: String,
    val city: String,
    @SerializedName("adcode") val adCode: String,
    val weather: String,
    @SerializedName("temperature_float") val temperature: Float,
    @SerializedName("winddirection") val windDirection: String,
    @SerializedName("windpower") val windPower: String,
    val humidity: Int,
    @SerializedName("reporttime") val reportTime: String
)

data class GaoDeDailyWeatherDTO(
    val status: Int,
    val info: String,
    @SerializedName("infocode") val infoCode: Int,
    val forecasts: List<Forecast>,
)

data class Forecast(
    val province: String,
    val city: String,
    @SerializedName("adcode") val adCode: String,
    @SerializedName("reporttime") val reportTime: String,
    val casts: List<Cast>,
)

data class Cast(
    val date: String,
    val week: Int,
    @SerializedName("dayweather") val dayCondition: String,
    @SerializedName("nightweather") val nightCondition: String,
    @SerializedName("daytemp_float") val dayTemp: Float,
    @SerializedName("nighttemp_float") val nightTemp: Float,
    @SerializedName("daywind") val dayWindDirection: String,
    @SerializedName("nightwind") val nightWindDirection: String,
    @SerializedName("daypower") val dayWindPower: String,
    @SerializedName("nightpower") val nightWindPower: String,
)

data class GaoDeSearchCityDTO(
    val status: Int,
    val info: String,
    @SerializedName("infocode") val infoCode: Int,
    @SerializedName("pois") val poiList: List<Poi>,
)

data class Poi(
    @SerializedName("adname") val adName: String,
    @SerializedName("adcode") val adCode: Int,
    @SerializedName("pname") val pName: String
)
