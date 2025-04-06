package com.lunacattus.clean.presentation.features.home.viewmodel

import com.lunacattus.clean.presentation.common.ui.base.BaseViewModel
import com.lunacattus.clean.presentation.features.home.mvi.HomeUiIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeUiIntent>() {
    override fun handleUiIntent(intent: HomeUiIntent) {

    }
}