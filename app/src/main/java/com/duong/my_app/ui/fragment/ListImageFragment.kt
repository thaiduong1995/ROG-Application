package com.duong.my_app.ui.fragment

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.duong.my_app.R
import com.duong.my_app.base.BaseFragment
import com.duong.my_app.data.model.Image
import com.duong.my_app.data.reponse.ImageState
import com.duong.my_app.databinding.FragmentListImageBinding
import com.duong.my_app.ui.adapter.ImageAdapter
import com.duong.my_app.utls.rcv.addItemDecoration
import com.duong.my_app.vm.ListImageViewModel
import com.duong.my_app.vm.ShareViewModel
import kotlinx.coroutines.launch

class ListImageFragment : BaseFragment() {

    override val viewModel by viewModels<ListImageViewModel>()

    private lateinit var binding: FragmentListImageBinding
    private val navArgs by navArgs<ListImageFragmentArgs>()
    private val shareViewModel by activityViewModels<ShareViewModel>()
    private val imageAdapter = ImageAdapter()
    private val listImageSelected = mutableListOf<Image>()
    private val listPositionSelected = mutableListOf<Int>()
    private var isSelectAll = false
    private var isMove = false
    private var isRestore = false
    private var isDelete = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initData() {
        super.initData()
        imageAdapter.setData(navArgs.listImage.toList())
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.imageStateFlow.collect {
                        observeImageState(it)
                    }
                }
                launch {
                    shareViewModel.removePositionFlow.collect {
                        observeRemovePosition(it)
                    }
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
                when {
                    isMove -> {
                        shareViewModel.setImageSize(imageState.listImage.size)
                        viewModel.navigateBack()
                    }
                    isRestore || isDelete -> {
                        imageAdapter.removeItem(listPositionSelected)
                    }
                }
                isMove = false
                isRestore = false
                isDelete = false
            }
        }
    }

    private fun observeRemovePosition(position: Int) {
        if (position > -1) {
            imageAdapter.removeItem(position)
        }
    }

    override fun initUi() {
        with(binding.tvTitle) {
            if (navArgs.title == null) isGone = true else text = navArgs.title
        }
        binding.rcvImage.apply {
            adapter = imageAdapter
            addItemDecoration(
                resources.getDimensionPixelOffset(R.dimen.size_5),
                resources.getDimensionPixelOffset(R.dimen.size_5)
            )
        }
    }

    override fun initListener() {
        binding.apply {
            imgBack.setOnClickListener {
                viewModel.navigateBack()
            }
            tvOpen.setOnClickListener {
                isMove = true
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    requiredPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                } else {
                    viewModel.moveImage(folderPath = navArgs.folderPath, listImage = listImageSelected)
                }
            }
            tvSelectAll.setOnClickListener {
                onSelectAllListener()
            }
            imgSelectAll.setOnClickListener {
                onSelectAllListener()
            }
            tvRestore.setOnClickListener {
                onRestoreListener()
            }
            tvDelete.setOnClickListener {
                onDeleteListener()
            }
        }
        imageAdapter.apply {
            onClickItemListener = { image, position ->
                if (listImageSelected.isEmpty()) {
                    viewModel.gotoImagePreview(image, navArgs.listImage)
                }
            }
            onSelectItemListener = { image, position ->
                if (image.isSelected) listImageSelected.add(image) else listImageSelected.remove(image)
                if (image.isSelected) listPositionSelected.add(position) else listPositionSelected.remove(position)
                if (listImageSelected.isEmpty()) {
                    imageAdapter.removeSelect()
                    showBottomMenu(false)
                } else {
                    showBottomMenu(true)
                }
            }
            onSelectAllListener = { listImage ->
                if (listImage.first().isSelected) {
                    listImageSelected.apply {
                        clear()
                        addAll(listImage)
                    }
                    showBottomMenu(true)
                } else {
                    listImageSelected.clear()
                    showBottomMenu(false)
                }
            }
        }
    }

    private fun onSelectAllListener() {
        isSelectAll = !isSelectAll
        imageAdapter.selectAll(isSelectAll)
        binding.imgSelectAll.setImageResource(
            if (isSelectAll) R.drawable.ic_circle_green else R.drawable.ic_circle_stroke_white
        )
    }

    private fun onRestoreListener() {
        isRestore = true
        viewModel.moveImage(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath,
            listImageSelected
        )
    }

    private fun onDeleteListener() {
        isDelete = true
        viewModel.deleteFile(listImageSelected)
    }

    private fun showBottomMenu(isShow: Boolean) {
        navArgs.title?.let {
            binding.apply {
                tvRestore.isGone = !isShow
                tvDelete.isGone = !isShow
            }
        }
    }

    private val requiredPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            viewModel.moveImage(folderPath = navArgs.folderPath, listImage = listImageSelected)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearData()
        shareViewModel.clearImageOption()
    }

    companion object {

        @JvmStatic
        fun newInstance() = ListImageFragment().apply {
            arguments = bundleOf()
        }
    }
}