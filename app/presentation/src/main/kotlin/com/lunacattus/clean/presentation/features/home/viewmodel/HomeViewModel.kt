package com.lunacattus.clean.presentation.features.home.viewmodel

import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.common.ui.base.BaseViewModel
import com.lunacattus.clean.presentation.features.home.mvi.HomeSideEffect
import com.lunacattus.clean.presentation.features.home.mvi.HomeUiIntent
import com.lunacattus.clean.presentation.features.home.mvi.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() :
    BaseViewModel<HomeUiIntent, HomeUiState, HomeSideEffect>() {

    init {
        Logger.d(TAG, "init.")
    }

    override val initUiState: HomeUiState get() = HomeUiState()

    override suspend fun processUiIntent(intent: HomeUiIntent) {
        when (intent) {
            HomeUiIntent.OnFeatureChatRequested -> {
                sendSideEffect(HomeSideEffect.NavigateToWeatherFeature)
            }
        }
    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}