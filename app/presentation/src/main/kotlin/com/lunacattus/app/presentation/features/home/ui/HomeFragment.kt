package com.lunacattus.app.presentation.features.home.ui

import android.Manifest
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.fragment.app.viewModels
import com.amap.api.maps.MapsInitializer
import com.lunacattus.app.presentation.common.navigation.NavCommand
import com.lunacattus.app.presentation.common.navigation.NavCommand.Companion.defaultNavDirection
import com.lunacattus.app.presentation.common.ui.base.BaseFragment
import com.lunacattus.app.presentation.features.home.mvi.HomeSideEffect
import com.lunacattus.app.presentation.features.home.mvi.HomeUiIntent
import com.lunacattus.app.presentation.features.home.mvi.HomeUiState
import com.lunacattus.app.presentation.features.home.viewmodel.HomeViewModel
import com.lunacattus.clean.common.Logger
import com.lunacattus.clean.presentation.R
import com.lunacattus.clean.presentation.databinding.FragmentHomeBinding
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeUiIntent, HomeUiState, HomeSideEffect, HomeViewModel>(
        FragmentHomeBinding::inflate
    ) {

    override val viewModel: HomeViewModel by viewModels()

    override fun setupViews(savedInstanceState: Bundle?) {
        Logger.d(TAG, "onViewCreated")
        setStatusBarColor()
        binding.btnFeatureChat.setOnClickListener {
            checkLocationPermission()
        }
    }

    override fun setupObservers() {

    }

    override fun handleSideEffect(effect: HomeSideEffect) {
        when (effect) {
            HomeSideEffect.NavigateToWeatherFeature -> {
                navCoordinator().execute(
                    NavCommand.ToDirection(
                        defaultNavDirection(R.id.action_home_to_weather)
                    )
                )
            }
        }
    }

    private fun checkLocationPermission() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    getString(R.string.location_permission_msg),
                    getString(R.string.ok),
                    getString(R.string.cancel)
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    getString(R.string.permission_goto_setting_msg),
                    getString(R.string.ok),
                    getString(R.string.cancel)
                )
            }
            .request { allGranted, _, _ ->
                if (allGranted) {
                    MapsInitializer.updatePrivacyShow(requireContext(), true, true)
                    MapsInitializer.updatePrivacyAgree(requireContext(), true)
                    dispatchUiIntent(HomeUiIntent.OnFeatureChatRequested)
                }
            }
    }

    private fun setStatusBarColor() {
        val isNightMode =
            requireActivity().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        Logger.d(TAG, "isNightMode: $isNightMode")
        if (isNightMode) {
            requireActivity().window.insetsController?.apply {
                setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
        } else {
            requireActivity().window.insetsController?.apply {
                setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
        }
    }

    companion object {
        const val TAG = "HomeFragment"
    }

}