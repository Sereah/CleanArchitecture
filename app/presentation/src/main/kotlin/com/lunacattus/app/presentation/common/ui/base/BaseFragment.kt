package com.lunacattus.app.presentation.common.ui.base

import android.os.Bundle
import android.os.Parcelable
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.lunacattus.app.presentation.common.di.NavCoordinatorEntryPoint
import com.lunacattus.app.presentation.common.navigation.NavCoordinator
import com.lunacattus.app.presentation.common.ui.dialog.DialogShareViewModel
import com.lunacattus.clean.common.Logger
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
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
    private val viewStates = SparseArray<Parcelable>()

    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.d(TAG, "onCreate: ${this.javaClass.simpleName}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Logger.d(TAG, "onCreateView: ${this.javaClass.simpleName}")
        if (_binding == null) {
            _binding = inflateBinding(inflater, container, false)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(savedInstanceState)
        setupObservers()
        observeSideEffect()
    }

    override fun onStart() {
        super.onStart()
        Logger.d(TAG, "onStart: ${this.javaClass.simpleName}")
    }

    override fun onResume() {
        super.onResume()
        Logger.d(TAG, "onResume: ${this.javaClass.simpleName}")
    }

    override fun onPause() {
        super.onPause()
        Logger.d(TAG, "onPause: ${this.javaClass.simpleName}")
    }

    override fun onStop() {
        super.onStop()
        Logger.d(TAG, "onStop: ${this.javaClass.simpleName}")
    }

    override fun onDestroyView() {
        Logger.d(TAG, "onDestroyView: ${this.javaClass.simpleName}")
        _binding = null
        super.onDestroyView()
    }

    override fun onDestroy() {
        Logger.d(TAG, "onDestroy: ${this.javaClass.simpleName}")
        viewStates.clear()
        super.onDestroy()
    }

    protected fun saveViewStates(vararg views: View) {
        viewStates.clear()
        views.forEach { view ->
            view.saveHierarchyState(viewStates)
        }
    }

    protected fun restoreViewStates(vararg views: View) {
        views.forEach { view ->
            view.restoreHierarchyState(viewStates)
        }
    }

    protected abstract fun setupViews(savedInstanceState: Bundle?)
    protected abstract fun setupObservers()
    protected abstract fun handleSideEffect(effect: EFFECT)

    protected inline fun <reified T : STATE, R> collectState(
        crossinline mapFn: (T) -> R,
        crossinline filterFn: (R) -> Boolean = { true },
        noinline collectFn: (R) -> Unit
    ) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .filterIsInstance<T>()
                    .map { mapFn(it) }
                    .filter { filterFn(it) }
                    .distinctUntilChanged()
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

    protected fun dialogResultState() = dialogViewModel.resultState

    private fun observeSideEffect() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sideEffect.collect { effect ->
                    handleSideEffect(effect)
                }
            }
        }
    }

    companion object {
        const val TAG = "BaseFragment"
    }
}