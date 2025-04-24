package com.lunacattus.app.presentation.features.home.ui

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import com.lunacattus.clean.presentation.R
import com.lunacattus.app.presentation.common.navigation.NavCommand
import com.lunacattus.app.presentation.common.ui.base.BaseFragment
import com.lunacattus.clean.presentation.databinding.FragmentHomeBinding
import com.lunacattus.app.presentation.features.home.mvi.HomeSideEffect
import com.lunacattus.app.presentation.features.home.mvi.HomeUiIntent
import com.lunacattus.app.presentation.features.home.mvi.HomeUiState
import com.lunacattus.app.presentation.features.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

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
                navCoordinator().execute(
                    NavCommand.ToDirection(
                        object : NavDirections {
                            override val actionId: Int = R.id.action_home_to_weather
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