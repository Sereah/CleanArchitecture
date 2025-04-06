package com.lunacattus.clean.presentation.features.resume.ui

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.common.ui.base.BaseFragment
import com.lunacattus.clean.presentation.common.ui.base.BaseViewModel
import com.lunacattus.clean.presentation.common.ui.dialog.MessageConfirmDialog
import com.lunacattus.clean.presentation.databinding.FragmentResumeEditBinding
import com.lunacattus.clean.presentation.features.resume.mvi.ResumeUiIntent
import com.lunacattus.clean.presentation.features.resume.viewmodel.ResumeViewModel
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

class ResumeEditFragment : BaseFragment<FragmentResumeEditBinding, ResumeUiIntent>(
    FragmentResumeEditBinding::inflate
) {
    private val viewModel: ResumeViewModel by navGraphViewModels(R.id.resume_navigation)

    override fun setupViews(savedInstanceState: Bundle?) {
        binding.text.setOnClickListener {
            findNavController().navigate(
                R.id.dialog_message_confirm_dest, bundleOf(
                    MessageConfirmDialog.VALUE_KEY to TAG
                )
            )
        }
    }

    override fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                dialogViewModel.resultState.mapNotNull { it }.collect {
                    if (it == true) {
                        binding.text.text = "true"
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        Logger.d(TAG, "onDestroyView")
        super.onDestroyView()
    }

    override fun onDestroy() {
        Logger.d(TAG, "onDestroy")
        super.onDestroy()
    }

    override fun setViewModel(): BaseViewModel<ResumeUiIntent> = viewModel

    companion object {
        const val TAG = "ResumeEditFragment"
    }
}