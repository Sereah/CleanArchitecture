package com.lunacattus.app.domain.model

data class Location(
    val longitude: Double,
    val latitude: Double,
    val country: String,
    val admin: String,
    val city: String,
    val countryCode: String
)