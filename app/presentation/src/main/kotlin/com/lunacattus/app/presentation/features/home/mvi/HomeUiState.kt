package com.lunacattus.app.presentation.features.home.mvi

import com.lunacattus.app.presentation.common.ui.base.IUiState

data class HomeUiState(
    val loading: Boolean = true
) : IUiState