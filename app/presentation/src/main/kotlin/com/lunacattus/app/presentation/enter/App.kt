package com.lunacattus.app.presentation.enter

import android.app.Application
import com.amap.api.maps.MapsInitializer
import com.lunacattus.data.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapsInitializer.initialize(this)
        MapsInitializer.setApiKey(BuildConfig.gaoDeSdkApi)
    }
}