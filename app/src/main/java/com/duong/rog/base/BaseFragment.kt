package com.duong.rog.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.duong.rog.R
import com.duong.rog.data.reponse.NavigationCommand
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    abstract val viewModel: BaseViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initUi()
        initListener()
    }

    open fun initData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.navigationFlow.collect {
                        handleNavigation(it)
                    }
                }
            }
        }
    }

    private fun handleNavigation(navCommand: NavigationCommand?) {
        when (navCommand) {
            NavigationCommand.ToBack -> findNavController().navigateUp()
            is NavigationCommand.ToDirection -> {
                try {
                    findNavController().navigate(navCommand.directions)
                } catch (ex: IllegalArgumentException) {
                    viewModel.navigateBack()
                }
            }

            null -> {}
        }
    }

    open fun initUi() {}
    open fun initListener() {}
}