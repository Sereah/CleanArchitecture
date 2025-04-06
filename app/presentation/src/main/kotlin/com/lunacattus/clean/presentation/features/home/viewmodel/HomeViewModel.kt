package com.lunacattus.clean.presentation.features.home.viewmodel

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.common.navigation.NavCommand
import com.lunacattus.clean.presentation.common.ui.base.BaseViewModel
import com.lunacattus.clean.presentation.features.home.mvi.HomeUiIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeUiIntent>() {

    override fun handleUiIntent(intent: HomeUiIntent) {
        when (intent) {
            HomeUiIntent.OnFeatureResumeRequested -> {
                viewModelScope.launch {
                    emitNavCommand(
                        NavCommand.ToDirection(
                            object : NavDirections {
                                override val actionId: Int = R.id.action_home_to_resume
                                override val arguments: Bundle = Bundle()
                            }
                        )
                    )
                }
            }
        }
    }
}