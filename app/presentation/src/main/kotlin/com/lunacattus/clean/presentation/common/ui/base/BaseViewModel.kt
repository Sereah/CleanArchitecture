package com.lunacattus.clean.presentation.common.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<INTENT : IUiIntent, STATE : IUiState, EFFECT : ISideEffect> :
    ViewModel() {

    private val _uiState = MutableStateFlow<STATE>(initUiState)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<EFFECT>(Channel.BUFFERED)
    val sideEffect = _effect.receiveAsFlow()

    abstract val initUiState: STATE

    abstract suspend fun processUiIntent(intent: INTENT)

    fun handleUiIntent(intent: INTENT) {
        viewModelScope.launch {
            processUiIntent(intent)
        }
    }

    protected fun sendSideEffect(effect: EFFECT) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}