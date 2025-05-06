package com.lunacattus.app.data.mapper

import android.location.Address
import com.lunacattus.app.data.remote.dto.GaoDeLocationInfoDTO
import com.lunacattus.app.domain.model.Location

object LocationMapper {

    fun android.location.Location.mapper(): Location {
        return Location(
            this.longitude,
            this.latitude,
            "",
            "",
            "",
            ""
        )
    }

    fun Address.mapper(): Location {
        return Location(
            this.longitude,
            this.latitude,
            this.countryName,
            this.adminArea,
            this.locality,
            this.countryCode
        )
    }

    fun GaoDeLocationInfoDTO.mapper(): Location {
        if (this.rectangle is String) {
            return Location(
                extractLanAndLon(this.rectangle).first,
                extractLanAndLon(this.rectangle).second,
                "",
                "",
                "",
                ""
            )
        } else {
            return Location(
                0.0,
                0.0,
                "",
                "",
                "",
                ""
            )
        }
    }

    private fun extractLanAndLon(input: String): Pair<Double, Double> {
        val pairs = input.split(";")
        if (pairs.isEmpty()) return Pair(0.0, 0.0)
        val firstPair = pairs[0]
        val coordinates = firstPair.split(",")
        if (coordinates.size != 2) return Pair(0.0, 0.0)
        val longitude = coordinates[0].toDoubleOrNull()
        val latitude = coordinates[1].toDoubleOrNull()
        if (longitude == null || latitude == null) return Pair(0.0, 0.0)
        return Pair(longitude, latitude)
    }
}