package com.duong.my_app.ui.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.duong.my_app.R
import com.duong.my_app.base.BaseFragment
import com.duong.my_app.data.model.Folder
import com.duong.my_app.data.model.Image
import com.duong.my_app.data.reponse.ImageState
import com.duong.my_app.databinding.FragmentFolderBinding
import com.duong.my_app.ui.adapter.FolderAdapter
import com.duong.my_app.ui.dialog.SetNameDialog
import com.duong.my_app.utls.rcv.addItemDecoration
import com.duong.my_app.vm.FolderViewModel
import com.duong.my_app.vm.ShareViewModel
import kotlinx.coroutines.launch
import java.io.File


class FolderFragment : BaseFragment() {

    override val viewModel by viewModels<FolderViewModel>()

    private lateinit var binding: FragmentFolderBinding
    private val shareViewModel by activityViewModels<ShareViewModel>()
    private val folderAdapter = FolderAdapter()
    private var folder: Folder? = null
    private var position = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFolderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun fetchData(context: Context) {
        super.fetchData(context)
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> viewModel.providerAllHiddenFolder()
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> viewModel.providerAllHiddenFolder()
        }
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
                launch {
                    viewModel.folderFlow.collect {
                        observeFolder(it)
                    }
                }
                launch {
                    viewModel.listImageFlow.collect {
                        observeListImage(it)
                    }
                }
                launch {
                    viewModel.imageStateFlow.collect {
                        observeImageState(it)
                    }
                }
                launch {
                    shareViewModel.imageSizeFlow.collect {
                        observeImageSize(it)
                    }
                }
            }
        }
    }

    private fun observeListFolder(list: List<Folder>) {
        folderAdapter.setData(list)
    }

    private fun observeFolder(folder: Folder?) {
        folder?.let { folderAdapter.insertItem(folder = it) }
    }

    private fun observeImageSize(imageSize: Int) {
        folder?.let {
            folderAdapter.updateItem(position = position, size = imageSize)
        }
    }

    private fun observeListImage(imageState: ImageState) {
        when (imageState) {
            is ImageState.Error -> {}
            ImageState.IDLE -> {}
            ImageState.Loading -> binding.viewLoading.root.isVisible = true
            is ImageState.Success -> {
                binding.viewLoading.root.isGone = true
                folder?.path?.let {
                    viewModel.goToFolderDetail(
                        title = null,
                        folderPath = it,
                        imageState.listImage.toTypedArray()
                    )
                }
            }
        }
    }

    private fun observeImageState(imageState: ImageState) {
        when (imageState) {
            is ImageState.Error -> {}
            ImageState.IDLE -> {}
            ImageState.Loading -> binding.viewLoading.root.isVisible = true
            is ImageState.Success -> {
                binding.viewLoading.root.isGone = true
                folderAdapter.updateItem(position, imageState.listImage.size)
            }
        }
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
                this@FolderFragment.folder = folder
                this@FolderFragment.position = position
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    checkDialogPermissionShowing(Manifest.permission.READ_MEDIA_IMAGES)
                    requestPermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    checkDialogPermissionShowing(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            onClickItemListener = { folder, position ->
                binding.viewLoading.root.isVisible = true
                val listImage = mutableListOf<Image>()
                File(folder.path).listFiles()?.forEach {
                    val image = Image(
                        name = it.name,
                        path = it.absolutePath
                    )
                    listImage.add(image)
                }
                viewModel.goToFolderDetail(
                    title = folder.name,
                    folderPath = folder.path,
                    listImage = listImage.toTypedArray()
                )
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

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                viewModel.getAllImage()
                onDismissPermissionDialog()
            } else {
                when {
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && !isShowDialogPermission -> alertDialog?.show()
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !isShowDialogPermission -> alertDialog?.show()
                }
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearData()
    }

    companion object {

        @JvmStatic
        fun newInstance() = FolderFragment().apply {
            arguments = bundleOf()
        }
    }
}