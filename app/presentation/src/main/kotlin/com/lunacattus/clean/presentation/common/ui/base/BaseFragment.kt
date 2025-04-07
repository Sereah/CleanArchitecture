package com.lunacattus.clean.presentation.common.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.lunacattus.clean.presentation.common.di.NavCoordinatorEntryPoint
import com.lunacattus.clean.presentation.common.navigation.NavCoordinator
import com.lunacattus.clean.presentation.common.ui.dialog.DialogShareViewModel
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

abstract class BaseFragment<
        VB : ViewBinding,
        INTENT : IUiIntent,
        STATE : IUiState,
        EFFECT : ISideEffect,
        VM : BaseViewModel<INTENT, STATE, EFFECT>>(
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
        observeSideEffect()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected abstract fun setupViews(savedInstanceState: Bundle?)
    protected abstract fun setupObservers()
    protected abstract fun handleSideEffect(effect: EFFECT)

    protected fun <T> observeUiState(
        selector: (STATE) -> T,
        useDistinct: Boolean = true,
        action: (T) -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val flow = viewModel.uiState
                    .mapNotNull { selector(it) }
                if (useDistinct) {
                    flow.distinctUntilChanged()
                }
                flow.collect { value -> action(value) }
            }
        }
    }

    private fun observeSideEffect() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sideEffect.collect { effect ->
                    handleSideEffect(effect)
                }
            }
        }
    }

    protected fun dispatchUiIntent(intent: INTENT) {
        viewModel.handleUiIntent(intent)
    }

    protected fun dialogResultState() = dialogViewModel.resultState

    protected fun getNavCoordinator(): NavCoordinator {
        val entryPoint = EntryPointAccessors.fromActivity(
            requireActivity(),
            NavCoordinatorEntryPoint::class.java
        )
        return entryPoint.navCoordinator()
    }
}