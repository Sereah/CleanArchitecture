package com.lunacattus.clean.presentation.features.resume.ui

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.common.ui.base.BaseFragment
import com.lunacattus.clean.presentation.common.ui.dialog.MessageConfirmDialog
import com.lunacattus.clean.presentation.databinding.FragmentResumeEditBinding
import com.lunacattus.clean.presentation.features.resume.mvi.ResumeUiIntent
import com.lunacattus.clean.presentation.features.resume.viewmodel.ResumeViewModel
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class ResumeEditFragment : BaseFragment<FragmentResumeEditBinding, ResumeUiIntent, ResumeViewModel>(
    FragmentResumeEditBinding::inflate
) {
    override val viewModel: ResumeViewModel by navGraphViewModels(R.id.resume_navigation)

    override fun setupViews(savedInstanceState: Bundle?) {
        binding.text.setOnClickListener {
            dispatchUiIntent(
                ResumeUiIntent.OnMsgConfirmDialogRequested(
                    bundleOf(MessageConfirmDialog.VALUE_KEY to "hello, dialog.")
                )
            )
        }
    }

    override fun setupObservers() {
        lifecycleScope.launch {
            dialogResultState().mapNotNull { it }
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect {
                    if (it is String) {
                        binding.text.text = it
                    }
                }
        }
    }

    companion object {
        const val TAG = "ResumeEditFragment"
    }
}