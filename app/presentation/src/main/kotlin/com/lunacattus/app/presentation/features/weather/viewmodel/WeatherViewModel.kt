package com.lunacattus.app.presentation.features.weather.viewmodel

import androidx.lifecycle.viewModelScope
import com.lunacattus.app.domain.model.WeatherGeo
import com.lunacattus.app.domain.usecase.location.GetLocationUseCase
import com.lunacattus.app.domain.usecase.weather.DeleteCityUseCase
import com.lunacattus.app.domain.usecase.weather.GetDailyWeatherUseCase
import com.lunacattus.app.domain.usecase.weather.GetHourlyWeatherUseCase
import com.lunacattus.app.domain.usecase.weather.GetNowWeatherUseCase
import com.lunacattus.app.domain.usecase.weather.GetWeatherGeoListUseCase
import com.lunacattus.app.domain.usecase.weather.QueryAllWeatherUseCase
import com.lunacattus.app.domain.usecase.weather.QueryGeoByIdUseCase
import com.lunacattus.app.domain.usecase.weather.RequestAndSaveWeatherUseCase
import com.lunacattus.app.domain.usecase.weather.UpdateSavedCityWeatherUseCase
import com.lunacattus.app.presentation.common.ui.base.BaseViewModel
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect.ShowFailToast
import com.lunacattus.app.presentation.features.weather.mvi.WeatherSideEffect.ShowWeatherDetailPage
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiIntent
import com.lunacattus.app.presentation.features.weather.mvi.WeatherUiState
import com.lunacattus.clean.common.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val locationUseCase: GetLocationUseCase,
    private val requestAndSaveWeatherUseCase: RequestAndSaveWeatherUseCase,
    private val updateSavedCityWeatherUseCase: UpdateSavedCityWeatherUseCase,
    private val queryAllWeatherUseCase: QueryAllWeatherUseCase,
    private val getWeatherGeoListUseCase: GetWeatherGeoListUseCase,
    private val getNowWeatherUseCase: GetNowWeatherUseCase,
    private val getDailyWeatherUseCase: GetDailyWeatherUseCase,
    private val getHourlyWeatherUseCase: GetHourlyWeatherUseCase,
    private val queryGeoByIdUseCase: QueryGeoByIdUseCase,
    private val deleteCityUseCase: DeleteCityUseCase
) : BaseViewModel<WeatherUiIntent, WeatherUiState, WeatherSideEffect>() {

    init {
        Logger.d(TAG, "init.")
        updateUiState { WeatherUiState.Loading }
        observeCurrentLocation()
        updateCitiesWeather()
        observeAllWeather()
    }

    override val initUiState: WeatherUiState get() = WeatherUiState.Initial

    override fun processUiIntent(intent: WeatherUiIntent) {
        viewModelScope.launch {
            when (intent) {
                is WeatherUiIntent.SearchCity -> searchCity(intent.key)
                is WeatherUiIntent.SearchCityWeather -> searchWeather(intent.geo)
                is WeatherUiIntent.AddCity -> addCity(intent.id)
                is WeatherUiIntent.QueryCity -> queryCity(intent.id)
                is WeatherUiIntent.DeleteCity -> deleteCity(intent.id)
                is WeatherUiIntent.SelectCityPage -> selectCityPage(intent.id)
            }
        }
    }

    override fun onCleared() {
        Logger.d(TAG, "onCleared.")
    }

    private fun observeCurrentLocation() {
        viewModelScope.launch {
            locationUseCase.invoke().filterNotNull().distinctUntilChanged().collect { location ->
                Logger.d(TAG, "collect current location: $location")
                if (location.latitude == 0.0 && location.longitude == 0.0) {
                    sendSideEffect(
                        ShowFailToast(
                            msg = "get location fail"
                        )
                    )
                    return@collect
                }
                delay(500)
                requestAndSaveWeatherUseCase.invoke(
                    "${location.longitude},${location.latitude}",
                    true
                ).onSuccess {
                    Logger.d(TAG, "request and save current location weather success!")
                }.onFailure {
                    sendSideEffect(
                        ShowFailToast(
                            msg = it.localizedMessage ?: ""
                        )
                    )
                }
            }
        }
    }

    private fun updateCitiesWeather() {
        viewModelScope.launch {
            delay(3 * 1000)
            updateSavedCityWeatherUseCase.invoke().onSuccess {
                Logger.d(TAG, "updateCitiesWeather success.")
            }
        }
    }

    private fun observeAllWeather() {
        viewModelScope.launch {
            queryAllWeatherUseCase.invoke().onSuccess { flow ->
                flow.filter { it.isNotEmpty() }.distinctUntilChanged().collect { weather ->
                    Logger.d(TAG, "query all weather: ${weather.size}")
                    updateUiState { current ->
                        when (current) {
                            is WeatherUiState.Success -> current.copy(weatherList = weather)
                            else -> WeatherUiState.Success(weatherList = weather)
                        }
                    }
                }
            }.onFailure {
                sendSideEffect(ShowFailToast(msg = it.localizedMessage ?: ""))
            }
        }
    }

    private suspend fun deleteCity(id: String) {
        deleteCityUseCase.invoke(id).onSuccess {
            Logger.d(TAG, "delete name: $id success.")
        }.onFailure {
            Logger.e(TAG, "delete name: $id fail, $it")
        }
    }

    private suspend fun searchCity(keyWord: String) {
        getWeatherGeoListUseCase.invoke(keyWord).onSuccess { list ->
            updateUiState { current ->
                when (current) {
                    is WeatherUiState.Success -> current.copy(searchGeoList = list)
                    else -> WeatherUiState.Success(searchGeoList = list)
                }
            }
        }.onFailure {
            sendSideEffect(
                ShowFailToast(
                    msg = it.localizedMessage ?: ""
                )
            )
        }
    }

    private suspend fun searchWeather(geo: WeatherGeo) {
        sendSideEffect(ShowWeatherDetailPage(geo))
        getNowWeatherUseCase.invoke(geo.id).onSuccess { now ->
            updateUiState { current ->
                when (current) {
                    is WeatherUiState.Success -> current.copy(searchNowWeather = now)
                    else -> WeatherUiState.Success(searchNowWeather = now)
                }
            }
        }.onFailure {
            sendSideEffect(
                ShowFailToast(
                    msg = it.localizedMessage ?: ""
                )
            )
        }
        getDailyWeatherUseCase.invoke(geo.id).onSuccess { daily ->
            updateUiState { current ->
                when (current) {
                    is WeatherUiState.Success -> current.copy(searchDailyWeather = daily)
                    else -> WeatherUiState.Success(searchDailyWeather = daily)
                }
            }
        }.onFailure {
            sendSideEffect(
                ShowFailToast(
                    msg = it.localizedMessage ?: ""
                )
            )
        }
        getHourlyWeatherUseCase.invoke(geo.id).onSuccess { hourly ->
            updateUiState { current ->
                when (current) {
                    is WeatherUiState.Success -> current.copy(searchHourlyWeather = hourly)
                    else -> WeatherUiState.Success(searchHourlyWeather = hourly)
                }
            }
        }.onFailure {
            sendSideEffect(
                ShowFailToast(
                    msg = it.localizedMessage ?: ""
                )
            )
        }
    }

    private suspend fun addCity(id: String) {
        sendSideEffect(WeatherSideEffect.BackToCityOptionPage)
        requestAndSaveWeatherUseCase.invoke(id, false)
    }

    private suspend fun queryCity(id: String) {
        queryGeoByIdUseCase.invoke(id).onSuccess { geo ->
            updateUiState { current ->
                when (current) {
                    is WeatherUiState.Success -> current.copy(queryGeo = geo)
                    else -> WeatherUiState.Success(queryGeo = geo)
                }
            }
        }
    }

    private fun selectCityPage(id: String) {
        updateUiState { current ->
            when (current) {
                is WeatherUiState.Success -> current.copy(selectedCityId = id)
                else -> WeatherUiState.Success(selectedCityId = id)
            }
        }
    }

    companion object {
        const val TAG = "WeatherViewModel"
    }
}