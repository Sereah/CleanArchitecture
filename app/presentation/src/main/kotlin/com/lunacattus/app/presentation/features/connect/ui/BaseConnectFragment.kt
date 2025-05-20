package com.lunacattus.app.presentation.features.connect.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.viewbinding.ViewBinding
import com.lunacattus.app.presentation.common.ui.base.BaseFragment
import com.lunacattus.app.presentation.features.connect.mvi.ConnectSideEffect
import com.lunacattus.app.presentation.features.connect.mvi.ConnectUiIntent
import com.lunacattus.app.presentation.features.connect.mvi.ConnectUiState
import com.lunacattus.app.presentation.features.connect.viewmodel.ConnectViewModel
import com.lunacattus.clean.presentation.R

abstract class BaseConnectFragment<VB : ViewBinding>(inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> VB) :
    BaseFragment<VB, ConnectUiIntent, ConnectUiState, ConnectSideEffect, ConnectViewModel>(
        inflateBinding
    ) {
    override val viewModel: ConnectViewModel by hiltNavGraphViewModels(R.id.connect_navigation)
}