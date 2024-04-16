package com.duong.my_app.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.room.util.query
import com.duong.my_app.R
import com.duong.my_app.base.BaseFragment
import com.duong.my_app.data.model.AppData
import com.duong.my_app.data.reponse.AppState
import com.duong.my_app.databinding.FragmentAppManagerBinding
import com.duong.my_app.extension.requestManagerApp
import com.duong.my_app.ui.adapter.AppAdapter
import com.duong.my_app.utls.rcv.addItemDecoration
import com.duong.my_app.vm.AppManagerViewModel
import kotlinx.coroutines.launch

class AppManagerFragment : BaseFragment() {

    override val viewModel by viewModels<AppManagerViewModel>()

    private lateinit var binding: FragmentAppManagerBinding
    private val appAdapter = AppAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAppManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun fetchData(context: Context) {
        super.fetchData(context)
        requestPermission(context)
    }

    override fun initData() {
        super.initData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.listAppFlow.collect {
                        observeListApp(it)
                    }
                }
            }
        }
    }

    private fun observeListApp(appState: AppState) {
        when (appState) {
            is AppState.Error -> {}
            AppState.IDLE -> {}
            AppState.Loading -> binding.progressBar.isVisible = true
            is AppState.Success -> {
                binding.progressBar.isGone = true
                appAdapter.setData(appState.listApp)
            }
        }
    }

    override fun initUi() {
        binding.rcvApp.apply {
            adapter = appAdapter
            addItemDecoration(
                resources.getDimensionPixelOffset(R.dimen.size_0),
                resources.getDimensionPixelOffset(R.dimen.size_10),
            )
        }
    }

    override fun initListener() {
        binding.apply {
            edtSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { query -> appAdapter.filter.filter(query) }
                    return false
                }

            })
            btnRequest.setOnClickListener {
                requestPermission.launch(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
            }
        }
        appAdapter.onClickItemListener = { appData, position ->
            goToSettingApp(appData)
        }
    }

    private fun goToSettingApp(appData: AppData) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", appData.packageName, null)
        intent.data = uri
        context?.startActivity(intent)
    }


    private fun requestPermission(context: Context) {
        if (context.requestManagerApp()) {
            viewModel.providerListAllApp()
        } else {
            requestPermission.launch(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
        }
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (context?.requestManagerApp() == true) {
                viewModel.providerListAllApp()
                binding.apply {
                    layoutSearch.isVisible = true
                    btnRequest.isGone = true
                }
            } else {
                binding.apply {
                    layoutSearch.isGone = true
                    btnRequest.isVisible = true
                }
            }
        }

    companion object {

        @JvmStatic
        fun newInstance() = AppManagerFragment().apply {
            arguments = bundleOf()
        }
    }
}