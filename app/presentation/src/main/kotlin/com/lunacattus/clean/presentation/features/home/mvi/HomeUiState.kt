package com.lunacattus.clean.presentation.features.home.mvi

import com.lunacattus.clean.presentation.common.ui.base.IUiState

data class HomeUiState(
    val loading: Boolean = true
) : IUiState