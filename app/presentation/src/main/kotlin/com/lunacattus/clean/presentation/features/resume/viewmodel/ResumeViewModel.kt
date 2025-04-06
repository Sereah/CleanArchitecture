package com.lunacattus.clean.presentation.features.resume.viewmodel

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.common.navigation.NavCommand.ToDirection
import com.lunacattus.clean.presentation.common.ui.base.BaseViewModel
import com.lunacattus.clean.presentation.features.resume.mvi.ResumeUiIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResumeViewModel @Inject constructor() : BaseViewModel<ResumeUiIntent>() {

    init {
        Logger.d(TAG, "init.")
    }

    override fun handleUiIntent(intent: ResumeUiIntent) {
        when (intent) {
            ResumeUiIntent.OnEditRequested -> {
                viewModelScope.launch {
                    emitNavCommand(
                        ToDirection(
                            object : NavDirections {
                                override val actionId: Int
                                    get() = R.id.action_resume_to_resumeEdit
                                override val arguments: Bundle
                                    get() = Bundle()
                            }
                        ))
                }
            }

            is ResumeUiIntent.OnMsgConfirmDialogRequested -> {
                viewModelScope.launch {
                    emitNavCommand(
                        ToDirection(
                        object : NavDirections {
                            override val actionId: Int
                                get() = R.id.dialog_message_confirm_dest
                            override val arguments: Bundle
                                get() = intent.bundle
                        }
                    ))
                }
            }
        }
    }

    override fun onCleared() {
        Logger.d(TAG, "onCleared.")
    }

    companion object {
        const val TAG = "ResumeViewModel"
    }
}