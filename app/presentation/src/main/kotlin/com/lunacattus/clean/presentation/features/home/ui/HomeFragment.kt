package com.lunacattus.clean.presentation.features.home.ui

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.lunacattus.clean.presentation.common.ui.base.BaseFragment
import com.lunacattus.clean.presentation.databinding.FragmentHomeBinding
import com.lunacattus.clean.presentation.features.home.mvi.HomeUiIntent
import com.lunacattus.clean.presentation.features.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeUiIntent, HomeViewModel>(
    FragmentHomeBinding::inflate
) {

    override val viewModel: HomeViewModel by viewModels()

    override fun setupViews(savedInstanceState: Bundle?) {
        binding.btnResume.setOnClickListener {
            dispatchUiIntent(HomeUiIntent.OnFeatureResumeRequested)
        }
    }

    override fun setupObservers() {

    }
}