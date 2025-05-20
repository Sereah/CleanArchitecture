package com.lunacattus.app.presentation.features.connect.mvi

import com.lunacattus.app.presentation.common.ui.base.IUiState

sealed interface ConnectUiState : IUiState {
    object Initial : ConnectUiState
}