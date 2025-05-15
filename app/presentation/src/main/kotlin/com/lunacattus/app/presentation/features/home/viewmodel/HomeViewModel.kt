package com.lunacattus.app.presentation.features.home.viewmodel

import com.lunacattus.app.presentation.common.ui.base.BaseViewModel
import com.lunacattus.app.presentation.features.home.mvi.HomeSideEffect
import com.lunacattus.app.presentation.features.home.mvi.HomeUiIntent
import com.lunacattus.app.presentation.features.home.mvi.HomeUiState
import com.lunacattus.clean.common.Logger
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() :
    BaseViewModel<HomeUiIntent, HomeUiState, HomeSideEffect>() {

    init {
        Logger.d(TAG, "init.")
    }

    override fun onCleared() {
        Logger.d(TAG, "onCleared.")
        super.onCleared()
    }

    override val initUiState: HomeUiState get() = HomeUiState.Initial

    override fun processUiIntent(intent: HomeUiIntent) {

    }

    companion object {
        const val TAG = "HomeViewModel"
    }
}