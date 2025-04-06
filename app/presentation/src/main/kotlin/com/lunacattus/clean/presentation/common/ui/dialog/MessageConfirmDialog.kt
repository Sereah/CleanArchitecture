package com.lunacattus.clean.presentation.common.ui.dialog

import android.os.Bundle
import com.lunacattus.clean.presentation.common.ui.base.BaseDialogFragment
import com.lunacattus.clean.presentation.databinding.DialogMessageConfirmBinding

class MessageConfirmDialog : BaseDialogFragment<DialogMessageConfirmBinding>(
    DialogMessageConfirmBinding::inflate
) {

    private val text: String by lazy { arguments?.getString(VALUE_KEY) ?: "" }

    override fun setupView(savedInstanceState: Bundle?) {
        binding.text.text = text
        binding.btn.setOnClickListener {
            dialogViewModel.emitResult("Hi, fragment.")
            dismiss()
        }
    }

    companion object {
        const val TAG = "MessageConfirmDialog"
        const val VALUE_KEY = "MessageConfirmDialog_value"
    }
}