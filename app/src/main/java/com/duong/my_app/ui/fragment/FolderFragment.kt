package com.duong.my_app.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.duong.my_app.R
import com.duong.my_app.base.BaseFragment
import com.duong.my_app.base.BaseViewModel
import com.duong.my_app.data.model.Folder
import com.duong.my_app.databinding.FragmentFolderBinding
import com.duong.my_app.ui.adapter.FolderAdapter
import com.duong.my_app.ui.dialog.SetNameDialog
import com.duong.my_app.utls.rcv.addItemDecoration
import com.duong.my_app.vm.FolderViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FolderFragment : BaseFragment() {

    override val viewModel by viewModels<FolderViewModel>()

    private lateinit var binding: FragmentFolderBinding
    private val folderAdapter = FolderAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun fetchData(context: Context) {
        super.fetchData(context)
        viewModel.providerAllHiddenFolder()
    }

    override fun initData() {
        super.initData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.listFolderFlow.collect {
                        observeListFolder(it)
                    }
                }
            }
        }
    }

    private fun observeListFolder(list: List<Folder>) {
        folderAdapter.setData(list)
    }

    override fun initUi() {
        binding.rcvFolder.apply {
            adapter = folderAdapter
            addItemDecoration(
                resources.getDimensionPixelOffset(R.dimen.size_10),
                resources.getDimensionPixelOffset(R.dimen.size_5),
            )
        }
    }
    override fun initListener() {
        binding.btnCreate.setOnClickListener {
            onCreateFolderListener()
        }
        folderAdapter.apply {
            onAddItemListener = { folder, position ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    selectImageOnDevice()
                } else requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            onClickItemListener = { folder, position ->

            }
        }
    }

    private fun onCreateFolderListener() {
        val setNameDialog = SetNameDialog.newInstance()
        if (!setNameDialog.isShown) setNameDialog.show(childFragmentManager, "")
        setNameDialog.onSetNameListener = { name ->
            viewModel.createFolder(folderName = name)
        }
    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        isGrated ->
            if (isGrated) {
                selectImageOnDevice()
            }
        }

    private fun selectImageOnDevice() {
        startForResult.launch(
            Intent().apply {
                type = "image/*"
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                action = Intent.ACTION_GET_CONTENT
            }
        )
    }

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d(TAG, "result = $result")

            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data?.clipData?.
                Log.d(TAG, "data = $data")
                if (data != null) {

                }
            }
        }


    companion object {

        @JvmStatic
        fun newInstance() = FolderFragment().apply {
                arguments = bundleOf()
            }
    }
}