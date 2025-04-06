package com.lunacattus.clean.presentation.common.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.lunacattus.clean.presentation.common.di.NavCoordinatorEntryPoint
import com.lunacattus.clean.presentation.common.navigation.NavCoordinator
import com.lunacattus.clean.presentation.common.ui.dialog.DialogShareViewModel
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.launch

abstract class BaseFragment<VB : ViewBinding, INTENT : IUiIntent, VM : BaseViewModel<INTENT>>(
    private val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!
    private val dialogViewModel: DialogShareViewModel by activityViewModels()
    protected abstract val viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(savedInstanceState)
        setupObservers()
        observeNavEvents()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    abstract fun setupViews(savedInstanceState: Bundle?)

    abstract fun setupObservers()

    protected fun dispatchUiIntent(intent: INTENT) {
        viewModel.handleUiIntent(intent)
    }

    protected fun dialogResultState() = dialogViewModel.resultState

    private fun observeNavEvents() {
        lifecycleScope.launch {
            viewModel.navEvents
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { command ->
                    getNavCoordinator().execute(command)
                }
        }
    }

    private fun getNavCoordinator(): NavCoordinator {
        val entryPoint = EntryPointAccessors.fromActivity(
            requireActivity(),
            NavCoordinatorEntryPoint::class.java
        )
        return entryPoint.navCoordinator()
    }
}