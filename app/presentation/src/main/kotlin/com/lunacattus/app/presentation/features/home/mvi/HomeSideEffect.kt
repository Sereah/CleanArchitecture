package com.lunacattus.app.presentation.features.home.mvi

import com.lunacattus.app.presentation.common.ui.base.ISideEffect

sealed class HomeSideEffect : ISideEffect {
    data object NavigateToWeatherFeature : HomeSideEffect()
}