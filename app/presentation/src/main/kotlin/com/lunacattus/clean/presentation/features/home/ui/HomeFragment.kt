package com.lunacattus.clean.presentation.features.home.ui

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.common.navigation.NavCommand
import com.lunacattus.clean.presentation.common.ui.base.BaseFragment
import com.lunacattus.clean.presentation.databinding.FragmentHomeBinding
import com.lunacattus.clean.presentation.features.home.mvi.HomeSideEffect
import com.lunacattus.clean.presentation.features.home.mvi.HomeUiIntent
import com.lunacattus.clean.presentation.features.home.mvi.HomeUiState
import com.lunacattus.clean.presentation.features.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeUiIntent, HomeUiState, HomeSideEffect, HomeViewModel>(
        FragmentHomeBinding::inflate
    ) {

    override val viewModel: HomeViewModel by viewModels()

    override fun setupViews(savedInstanceState: Bundle?) {
        binding.btnFeatureChat.setOnClickListener {
            dispatchUiIntent(HomeUiIntent.OnFeatureChatRequested)
        }
    }

    override fun setupObservers() {

    }

    override fun handleSideEffect(effect: HomeSideEffect) {
        when (effect) {
            HomeSideEffect.NavigateToWeatherFeature -> {
                getNavCoordinator().execute(
                    NavCommand.ToDirection(
                        object : NavDirections {
                            override val actionId: Int = R.id.action_home_to_chat
                            override val arguments: Bundle = Bundle()
                        }
                    ))
            }
        }
    }

    companion object {
        const val TAG = "HomeFragment"
    }

}