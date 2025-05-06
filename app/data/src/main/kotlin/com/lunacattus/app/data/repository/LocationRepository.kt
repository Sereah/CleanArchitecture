package com.lunacattus.app.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Criteria
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.lunacattus.app.data.mapper.LocationMapper.mapper
import com.lunacattus.app.data.remote.datasource.GaoDeWeatherRemoteDataSource
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
    private val appContext: Context,
    private val gaoDeWeatherRemoteDataSource: GaoDeWeatherRemoteDataSource
) : ILocationRepository {

    @SuppressLint("MissingPermission")
    override fun getLocation(): Flow<Location> = callbackFlow {
        val callback = LocationListener { location ->
            Logger.d(TAG, "onLocationChanged: $location")
            val address = getAddress(location)
            if (address.isNullOrEmpty()) {
                trySend(location.mapper())
            } else {
                trySend(address[0].mapper())
            }
        }

        listOf(
            LocationManager.FUSED_PROVIDER,
            LocationManager.GPS_PROVIDER,
            LocationManager.NETWORK_PROVIDER,
            LocationManager.PASSIVE_PROVIDER,
        ).firstNotNullOfOrNull {
            locationManager.getLastKnownLocation(it)
        }.let { location ->
            Logger.d(TAG, "getLastKnownLocation: $location")
            if (location == null) {
                val result = gaoDeWeatherRemoteDataSource.getLocationByIp().mapper()
                Logger.d(TAG, "get location By IP: $result")
                trySend(result)
            } else {
                val address = getAddress(location)
                if (address.isNullOrEmpty()) {
                    trySend(location.mapper())
                } else {
                    trySend(address[0].mapper())
                }
            }
        }

        val criteria = Criteria().apply {
            accuracy = Criteria.ACCURACY_FINE
        }
        val provider = locationManager.getBestProvider(criteria, true)
        Logger.d(TAG, "getBestProvider: $provider")
        if (provider != null && locationManager.isProviderEnabled(provider)) {
            locationManager.requestLocationUpdates(
                provider,
                5000,
                10f,
                callback
            )
        } else {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                5000,
                10f,
                callback
            )
        }

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

    override fun getLocationByGaoDe(): Flow<Location> = callbackFlow {
        val listener = AMapLocationListener {
            Logger.d(TAG, "AMapLocationListener onChange: $it")
            it?.let {
                trySend(it.mapper())
            }
        }
        val locationOption = AMapLocationClientOption().apply {
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            interval = 60 * 1000
        }

        val locationClient = AMapLocationClient(appContext).apply {
            setLocationOption(locationOption)
            setLocationListener(listener)
        }

        locationClient.startLocation()

        awaitClose {
            Logger.d(TAG, "awaitClose")
            locationClient.stopLocation()
        }
    }

    companion object {
        const val TAG = "LocationRepository"
    }

}