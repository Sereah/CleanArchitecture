package com.lunacattus.clean.presentation.features.resume.ui

import android.os.Bundle
import androidx.navigation.navGraphViewModels
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.common.ui.base.BaseFragment
import com.lunacattus.clean.presentation.databinding.FragmentResumeBinding
import com.lunacattus.clean.presentation.features.resume.mvi.ResumeUiIntent
import com.lunacattus.clean.presentation.features.resume.viewmodel.ResumeViewModel

class ResumeFragment : BaseFragment<FragmentResumeBinding, ResumeUiIntent, ResumeViewModel>(
    FragmentResumeBinding::inflate
) {

    override val viewModel: ResumeViewModel by navGraphViewModels(R.id.resume_navigation)

    override fun setupViews(savedInstanceState: Bundle?) {
        binding.text.setOnClickListener {
            dispatchUiIntent(ResumeUiIntent.OnEditRequested)
        }
    }

    override fun setupObservers() {

    }

    companion object {
        const val TAG = "ResumeFragment"
    }

}