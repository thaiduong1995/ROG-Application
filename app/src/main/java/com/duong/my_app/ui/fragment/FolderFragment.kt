package com.duong.my_app.ui.fragment

import android.Manifest
import android.R.attr.path
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
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
            ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> viewModel.providerAllHiddenFolder()
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
                    shareViewModel.listImageSelectedFlow.collect {
                        observeListImageSelected(it)
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

    private fun observeListImageSelected(listImage: List<Image>) {
        folder?.let {
            viewModel.moveImage(folder = it, listImage = listImage)
            folderAdapter.updateItem(position = position, size = listImage.size)
        }
    }

    private fun observeListImage(imageState: ImageState) {
        when (imageState) {
            is ImageState.Error -> {}
            ImageState.IDLE -> {}
            ImageState.Loading -> binding.progressBar.isVisible = true
            is ImageState.Success -> {
                binding.progressBar.isGone = true
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
                    requestPermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
                } else requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            onClickItemListener = { folder, position ->
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                val uri = Uri.parse(
                    Environment.getExternalStorageState(File(folder.path))
                            + File.separator + ".${folder.name}" + File.separator
                )
                intent.setDataAndType(uri,"application/*") // or application/*
                startActivity(Intent.createChooser(intent, "Open folder"))

                val file: File = File(path)
                val uri_path = Uri.fromFile(file)
                val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    MimeTypeMap.getFileExtensionFromUrl(
                        path
                    )
                )


                val intent = Intent(Intent.ACTION_VIEW)
                intent.setType(mimeType)
                intent.setDataAndType(uri_path, mimeType)
                startActivity(intent)
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
                viewModel.goToImageFolder()
                onDismissPermissionDialog()
            } else {
                when {
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && !shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> alertDialog?.show()
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) -> alertDialog?.show()
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