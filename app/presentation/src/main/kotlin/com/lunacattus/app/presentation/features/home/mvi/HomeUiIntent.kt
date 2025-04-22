package com.lunacattus.app.presentation.features.home.mvi

import com.lunacattus.app.presentation.common.ui.base.IUiIntent

sealed class HomeUiIntent : IUiIntent {
    data object OnFeatureChatRequested : HomeUiIntent()
}