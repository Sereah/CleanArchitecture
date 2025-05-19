package com.lunacattus.app.presentation.features.home.ui

import android.Manifest
import android.content.res.Configuration
import android.os.Bundle
import android.view.WindowInsetsController
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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
import java.lang.ref.WeakReference

@AndroidEntryPoint
class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeUiIntent, HomeUiState, HomeSideEffect, HomeViewModel>(
        FragmentHomeBinding::inflate
    ) {

    private lateinit var featureAdapter: FeatureListAdapter

    override val viewModel: HomeViewModel by viewModels()

    override fun setupViews(savedInstanceState: Bundle?) {
        Logger.d(TAG, "onViewCreated")
        setStatusBarColor()
        featureAdapter = FeatureListAdapter(WeakReference(itemClickListener))
        binding.featureList.apply {
            adapter = featureAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        initList()
    }

    override fun onDestroyView() {
        binding.featureList.adapter = null
        super.onDestroyView()
    }

    override fun setupObservers() {

    }

    override fun handleSideEffect(effect: HomeSideEffect) {

    }

    private val itemClickListener: (String) -> Unit = { id ->
        checkFeature(id)
    }

    private fun checkFeature(id: String) {
        when {
            id == FEATURE_WEATHER -> {
                checkPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                ) {
                    MapsInitializer.updatePrivacyShow(requireContext(), true, true)
                    MapsInitializer.updatePrivacyAgree(requireContext(), true)
                    navCoordinator.execute(
                        NavCommand.ToDirection(
                            defaultNavDirection(R.id.action_home_to_weather)
                        )
                    )
                }
            }
        }
    }

    private fun checkPermission(vararg permission: String, success: () -> Unit) {
        PermissionX.init(this)
            .permissions(permission.toList())
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(
                    deniedList,
                    getString(R.string.request_permission_msg),
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
                    success()
                }
            }
    }

    private fun setStatusBarColor() {
        val isNightMode =
            requireActivity().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
                    Configuration.UI_MODE_NIGHT_YES
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

    private fun initList() {
        featureAdapter.submitList(
            listOf(
                FeatureListAdapter.Companion.FeatureItem(
                    name = FEATURE_WEATHER,
                    desc = "一个简单的天气应用，数据来源‘和风天气’和‘高德地图’。",
                    imgSource = R.drawable.img_feature_weather,
                    bgSource = R.drawable.bg_feature_weather
                )
            )
        )
    }

    companion object {
        const val TAG = "HomeFragment"
        const val FEATURE_WEATHER = "猫の天气"
    }

}