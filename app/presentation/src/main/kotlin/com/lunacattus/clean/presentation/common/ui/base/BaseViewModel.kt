package com.lunacattus.clean.presentation.common.ui.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel<INTENT : IUiIntent> : ViewModel() {

    abstract fun handleUiIntent(intent: INTENT)
}