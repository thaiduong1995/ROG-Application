package com.duong.my_app.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.duong.my_app.data.reponse.NavigationCommand
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    val TAG = this::class.simpleName

    abstract val viewModel: BaseViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fetchData(context)
    }

    open fun fetchData(context: Context) {}

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
                    viewModel.clearData()
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