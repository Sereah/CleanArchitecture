package com.lunacattus.app.data.mapper

import android.location.Address
import com.lunacattus.app.domain.model.Location

object LocationMapper {

    fun android.location.Location.mapper(): Location {
        return Location(
            this.latitude,
            this.longitude,
            "",
            "",
            "",
            ""
        )
    }

    fun Address.mapper(): Location {
        return Location(
            this.latitude,
            this.longitude,
            this.countryName,
            this.adminArea,
            this.locality,
            this.countryCode
        )
    }
}