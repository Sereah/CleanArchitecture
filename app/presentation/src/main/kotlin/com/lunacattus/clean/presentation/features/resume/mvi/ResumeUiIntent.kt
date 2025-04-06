package com.lunacattus.clean.presentation.features.resume.mvi

import android.os.Bundle
import com.lunacattus.clean.presentation.common.ui.base.IUiIntent

sealed class ResumeUiIntent : IUiIntent {
    data object OnEditRequested : ResumeUiIntent()
    data class OnMsgConfirmDialogRequested(val bundle: Bundle) : ResumeUiIntent()
}