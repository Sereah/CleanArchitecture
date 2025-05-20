package com.lunacattus.app.presentation.features.connect.viewmodel

import com.lunacattus.app.presentation.common.ui.base.BaseViewModel
import com.lunacattus.app.presentation.features.connect.mvi.ConnectSideEffect
import com.lunacattus.app.presentation.features.connect.mvi.ConnectUiIntent
import com.lunacattus.app.presentation.features.connect.mvi.ConnectUiState

class ConnectViewModel : BaseViewModel<ConnectUiIntent, ConnectUiState, ConnectSideEffect>() {

    override val initUiState: ConnectUiState get() = ConnectUiState.Initial

    override fun processUiIntent(intent: ConnectUiIntent) {

    }

}