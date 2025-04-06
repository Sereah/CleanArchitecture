package com.lunacattus.clean.presentation.common.ui.base

import androidx.lifecycle.ViewModel
import com.lunacattus.clean.presentation.common.navigation.NavCommand
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel<INTENT : IUiIntent> : ViewModel() {

    private val _navEvents = Channel<NavCommand>()
    val navEvents: Flow<NavCommand> = _navEvents.receiveAsFlow()

    abstract fun handleUiIntent(intent: INTENT)

    protected suspend fun emitNavCommand(event: NavCommand) {
        _navEvents.send(event)
    }
}