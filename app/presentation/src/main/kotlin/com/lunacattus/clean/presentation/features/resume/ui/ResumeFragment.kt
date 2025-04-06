package com.lunacattus.clean.presentation.features.resume.ui

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.common.ui.base.BaseFragment
import com.lunacattus.clean.presentation.common.ui.base.BaseViewModel
import com.lunacattus.clean.presentation.databinding.FragmentResumeBinding
import com.lunacattus.clean.presentation.features.resume.mvi.ResumeUiIntent
import com.lunacattus.clean.presentation.features.resume.viewmodel.ResumeViewModel

class ResumeFragment : BaseFragment<FragmentResumeBinding, ResumeUiIntent>(
    FragmentResumeBinding::inflate
) {

    private val viewModel: ResumeViewModel by navGraphViewModels(R.id.resume_navigation)

    override fun setupViews(savedInstanceState: Bundle?) {
        binding.text.setOnClickListener {
            findNavController().navigate(R.id.action_resume_to_resumeEdit)
        }
        viewModel.setValue()
    }

    override fun setupObservers() {

    }

    override fun setViewModel(): BaseViewModel<ResumeUiIntent> = viewModel

    companion object {
        const val TAG = "ResumeFragment"
    }

}