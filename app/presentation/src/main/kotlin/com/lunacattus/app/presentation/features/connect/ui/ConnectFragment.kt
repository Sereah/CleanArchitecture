package com.lunacattus.app.presentation.features.connect.ui

import android.os.Bundle
import com.lunacattus.app.presentation.features.connect.mvi.ConnectSideEffect
import com.lunacattus.clean.presentation.databinding.FragmentConnectBinding

class ConnectFragment : BaseConnectFragment<FragmentConnectBinding>(
    FragmentConnectBinding::inflate
) {
    
    override fun setupViews(savedInstanceState: Bundle?) {
        
    }

    override fun setupObservers() {
        
    }

    override fun handleSideEffect(effect: ConnectSideEffect) {
        
    }
}