package com.lunacattus.clean.presentation.features.resume.viewmodel

import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.common.ui.base.BaseViewModel
import com.lunacattus.clean.presentation.features.resume.mvi.ResumeUiIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResumeViewModel @Inject constructor() : BaseViewModel<ResumeUiIntent>() {

    override fun handleUiIntent(intent: ResumeUiIntent) {

    }

    fun setValue() {

    }

    override fun onCleared() {
        Logger.d(TAG, "onCleared")
    }

    companion object {
        const val TAG = "ResumeViewModel"
    }
}