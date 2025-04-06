package com.lunacattus.clean.presentation.common.ui.dialog

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class DialogShareViewModel @Inject constructor() : ViewModel() {

    private val _resultState = MutableStateFlow<Any?>(null)
    val resultState = _resultState.asStateFlow()

    fun emitResult(result: Any) {
        _resultState.update { result }
    }
}