package com.lunacattus.clean.presentation.common.ui.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import com.lunacattus.clean.presentation.common.ui.dialog.DialogShareViewModel

abstract class BaseDialogFragment<VB : ViewBinding>(
    private val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : DialogFragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!
    protected val dialogViewModel: DialogShareViewModel by activityViewModels()

    protected abstract fun setupView(savedInstanceState: Bundle?)
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
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
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