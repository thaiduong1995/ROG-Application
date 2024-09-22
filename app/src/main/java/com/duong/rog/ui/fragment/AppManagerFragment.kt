package com.duong.rog.ui.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.duong.rog.R
import com.duong.rog.base.BaseFragment
import com.duong.rog.data.model.AppData
import com.duong.rog.data.reponse.AppState
import com.duong.rog.data.reponse.ProgressBarState
import com.duong.rog.databinding.FragmentAppManagerBinding
import com.duong.rog.extension.requestManagerApp
import com.duong.rog.ui.adapter.AppAdapter
import com.duong.rog.utls.rcv.addItemDecoration
import com.duong.rog.vm.AppManagerViewModel
import com.duong.rog.vm.SharedViewModel
import kotlinx.coroutines.launch

class AppManagerFragment : BaseFragment() {

    override val viewModel by viewModels<AppManagerViewModel>()
    override val TAG = "AppManagerFragment"

    private var _binding: FragmentAppManagerBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private val appAdapter = AppAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppManagerBinding.inflate(inflater, container, false)
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
        Log.d("ProgressBarState", "$TAG - $appState")
        when (appState) {
            is AppState.Error -> {}
            AppState.IDLE -> {}
            AppState.Loading -> {
                sharedViewModel.setProgressBarState(ProgressBarState.LoadingData)
            }
            is AppState.Success -> {
                sharedViewModel.setProgressBarState(ProgressBarState.Idle)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        @JvmStatic
        fun newInstance() = AppManagerFragment().apply {
            arguments = bundleOf()
        }
    }
}