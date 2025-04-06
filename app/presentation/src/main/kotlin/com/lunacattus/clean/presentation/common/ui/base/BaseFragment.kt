package com.lunacattus.clean.presentation.common.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.lunacattus.clean.presentation.common.ui.dialog.DialogShareViewModel

abstract class BaseFragment<VB : ViewBinding, INTENT : IUiIntent>(
    private val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!
    protected val dialogViewModel: DialogShareViewModel by activityViewModels()

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
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    abstract fun setupViews(savedInstanceState: Bundle?)

    abstract fun setupObservers()

    abstract fun setViewModel(): BaseViewModel<INTENT>

    protected fun dispatchUiIntent(intent: INTENT) {
        setViewModel().handleUiIntent(intent)
    }
}