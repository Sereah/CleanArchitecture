package com.lunacattus.app.presentation.common.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.lunacattus.app.presentation.common.di.NavCoordinatorEntryPoint
import com.lunacattus.app.presentation.common.navigation.NavCoordinator
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class BaseDialogFragment<
        VB : ViewBinding,
        INTENT : IUiIntent,
        STATE : IUiState,
        EFFECT : ISideEffect,
        VM : BaseViewModel<INTENT, STATE, EFFECT>>(
    private val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : DialogFragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    abstract val viewModel: VM

    protected abstract fun setupView(savedInstanceState: Bundle?)
    protected abstract fun setupObservers()

    protected open fun provideDialogConfig(): DialogConfig {
        return DialogConfig()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        return dialog
    }

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
        applyWindowConfiguration()
        setupView(savedInstanceState)
        setupObservers()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    protected class DialogConfig(
        val widthDp: Int = WindowManager.LayoutParams.WRAP_CONTENT,
        val heightDp: Int = WindowManager.LayoutParams.WRAP_CONTENT,
        val gravity: Int = Gravity.CENTER,
        val marginX: Int = 0,
        val marginY: Int = 0,
        val outCancelable: Boolean = true,
    )

    data class FlowConfig<T, R>(
        val mapFn: (T) -> R,
        val filterFn: (R) -> Boolean = { true },
        val distinctFn: (old: R, new: R) -> Boolean = { old, new -> old == new }
    )

    protected inline fun <reified T : STATE, A, B> collectCombined(
        flowA: FlowConfig<T, A>,
        flowB: FlowConfig<T, B>,
        crossinline collectFn: (A, B) -> Unit
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val sharedFlow = viewModel.uiState.filterIsInstance<T>()

                val processedFlowA = sharedFlow
                    .map { flowA.mapFn(it) }
                    .filter { flowA.filterFn(it) }
                    .distinctUntilChanged(flowA.distinctFn)

                val processedFlowB = sharedFlow
                    .map { flowB.mapFn(it) }
                    .filter { flowB.filterFn(it) }
                    .distinctUntilChanged(flowB.distinctFn)

                processedFlowA.combine(processedFlowB) { a, b ->
                    collectFn(a, b)
                }.collect()
            }
        }
    }

    protected inline fun <reified T : STATE, R> collectState(
        crossinline mapFn: (T) -> R,
        crossinline filterFn: (R) -> Boolean = { true },
        noinline distinctUntilChangedFn: (old: R, new: R) -> Boolean = { old, new -> old == new },
        noinline collectFn: (R) -> Unit
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .filterIsInstance<T>()
                    .map { mapFn(it) }
                    .filter { filterFn(it) }
                    .distinctUntilChanged(distinctUntilChangedFn)
                    .collect { collectFn(it) }
            }
        }
    }

    protected inline fun <reified T : STATE> collectState(
        crossinline filterFn: (T) -> Boolean = { true },
        noinline collectFn: (T) -> Unit
    ) {
        collectState<T, T>(mapFn = { it }, filterFn = filterFn, collectFn = collectFn)
    }

    protected fun dispatchUiIntent(intent: INTENT) {
        viewModel.handleUiIntent(intent)
    }

    protected fun navCoordinator(): NavCoordinator {
        val entryPoint = EntryPointAccessors.fromActivity(
            requireActivity(),
            NavCoordinatorEntryPoint::class.java
        )
        return entryPoint.navCoordinator()
    }

    private fun applyWindowConfiguration() {
        val config = provideDialogConfig()
        val window = dialog?.window ?: return
        val params = window.attributes

        params.width = config.widthDp
        params.height = config.heightDp

        params.gravity = config.gravity
        if (config.gravity != Gravity.CENTER) {
            params.x = config.marginX
            params.y = config.marginY
        }
        window.attributes = params
        isCancelable = config.outCancelable
    }
}