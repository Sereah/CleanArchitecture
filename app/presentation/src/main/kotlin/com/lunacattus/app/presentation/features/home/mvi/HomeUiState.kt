package com.lunacattus.app.presentation.features.home.mvi

import com.lunacattus.app.presentation.common.ui.base.IUiState

sealed interface HomeUiState : IUiState {
    data object Initial : HomeUiState
    data object Loading : HomeUiState
    data class Success(val msg: String) : HomeUiState
    data class Error(val msg: String) : HomeUiState
}