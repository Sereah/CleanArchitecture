package com.lunacattus.app.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import com.lunacattus.app.data.mapper.LocationMapper.mapper
import com.lunacattus.app.domain.model.Location
import com.lunacattus.app.domain.repository.location.ILocationRepository
import com.lunacattus.clean.common.Logger
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    private val locationManager: LocationManager,
    private val appContext: Context
) : ILocationRepository {

    @SuppressLint("MissingPermission")
    override fun getLocation(): Flow<Location> = callbackFlow {
        val callback = object : LocationListener {
            override fun onLocationChanged(location: android.location.Location) {
                Logger.d(TAG, "onLocationChanged: $location")
                val address = getAddress(location)
                if (address == null) {
                    trySend(location.mapper())
                } else {
                    trySend(address[0].mapper())
                }
            }
        }

        listOf(
            LocationManager.GPS_PROVIDER,
            LocationManager.NETWORK_PROVIDER,
            LocationManager.PASSIVE_PROVIDER,
        ).firstNotNullOfOrNull {
            locationManager.getLastKnownLocation(it)
        }.let { location ->
            Logger.d(TAG, "getLastKnownLocation: $location")
            location?.let {
                val address = getAddress(it)
                if (address == null) {
                    trySend(it.mapper())
                } else {
                    trySend(address[0].mapper())
                }
            }
        }

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            5000,
            10f,
            callback
        )

        awaitClose {
            Logger.d(TAG, "awaitClose")
            locationManager.removeUpdates(callback)
        }
    }

    private fun getAddress(location: android.location.Location): List<Address>? {
        val geocoder = Geocoder(appContext, Locale.getDefault())
        return try {
            geocoder.getFromLocation(location.latitude, location.longitude, 1)
        } catch (e: Exception) {
            Logger.e(TAG, e.toString())
            null
        }
    }

    companion object {
        const val TAG = "LocationRepository"
    }

}