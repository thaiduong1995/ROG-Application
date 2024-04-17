package com.duong.my_app.base

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.duong.my_app.R
import com.duong.my_app.data.reponse.NavigationCommand
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
abstract class BaseFragment : Fragment() {

    val TAG = this::class.simpleName

    abstract val viewModel: BaseViewModel
    var alertDialog: AlertDialog? = null
        private set

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fetchData(context)
    }

    open fun fetchData(context: Context) {
        alertDialog = AlertDialog.Builder(context)
            .setMessage(getString(R.string.request_permission))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                openAppSettings()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                onDismissPermissionDialog()
            }
            .setCancelable(false)
            .create()
    }

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
    open fun onDismissPermissionDialog() {
        if (alertDialog?.isShowing == true) {
            alertDialog?.dismiss()
        }
    }

    private fun openAppSettings() {
        context?.let { ct ->
            val packageUri = Uri.fromParts("package", ct.applicationContext.packageName, null)
            val applicationDetailsSettingsIntent = Intent()
            applicationDetailsSettingsIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            applicationDetailsSettingsIntent.setData(packageUri)
            applicationDetailsSettingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ct.applicationContext.startActivity(applicationDetailsSettingsIntent)
        }
    }
}