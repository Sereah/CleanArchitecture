package com.lunacattus.clean.presentation.features.home.ui

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.common.ui.base.BaseFragment
import com.lunacattus.clean.presentation.common.ui.base.BaseViewModel
import com.lunacattus.clean.presentation.databinding.FragmentHomeBinding
import com.lunacattus.clean.presentation.features.home.mvi.HomeUiIntent
import com.lunacattus.clean.presentation.features.home.viewmodel.HomeViewModel

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeUiIntent>(
    FragmentHomeBinding::inflate
) {

    private val viewModel: HomeViewModel by viewModels()

    override fun setViewModel(): BaseViewModel<HomeUiIntent> = viewModel

    override fun setupViews(savedInstanceState: Bundle?) {
        binding.btnResume.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_resume)
        }
    }

    override fun setupObservers() {

    }
}