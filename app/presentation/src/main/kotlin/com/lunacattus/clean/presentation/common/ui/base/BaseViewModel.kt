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

    private val _uiState = MutableStateFlow(initUiState)
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<EFFECT>(Channel.BUFFERED)
    val sideEffect = _effect.receiveAsFlow()

    abstract val initUiState: STATE

    abstract fun processUiIntent(intent: INTENT)

    fun handleUiIntent(intent: INTENT) {
        viewModelScope.launch {
            processUiIntent(intent)
        }
    }

    protected fun updateUiState(update: (STATE) -> STATE) {
        val currentState = _uiState.value
        val newState = update(currentState)
        _uiState.value = newState
    }

    protected fun sendSideEffect(effect: EFFECT) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }
}