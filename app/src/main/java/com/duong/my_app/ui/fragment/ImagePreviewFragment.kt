package com.duong.my_app.ui.fragment

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.duong.my_app.base.BaseFragment
import com.duong.my_app.data.model.Image
import com.duong.my_app.data.reponse.ImageState
import com.duong.my_app.databinding.FragmentImagePreviewBinding
import com.duong.my_app.ui.adapter.ViewPagerAdapter
import com.duong.my_app.ui.dialog.DialogDelete
import com.duong.my_app.vm.ImagePreviewViewModel
import com.duong.my_app.vm.ListImageViewModel
import com.duong.my_app.vm.ShareViewModel
import kotlinx.coroutines.launch

class ImagePreviewFragment : BaseFragment() {

    override val viewModel by viewModels<ImagePreviewViewModel>()

    private lateinit var binding: FragmentImagePreviewBinding
    private val navArgs by navArgs<ImagePreviewFragmentArgs>()
    private val shareViewModel by activityViewModels<ShareViewModel>()
    private val listImageViewModel by viewModels<ListImageViewModel>()
    private val listImage = mutableListOf<Image>()
    private val listDelete = mutableListOf<Int>()
    private var viewPageAdapter: ViewPagerAdapter? = null
    private var selectedPosition = 0
    private var isPreview = false
    private var deg = 0f

    override fun fetchData(context: Context) {
        super.fetchData(context)
        listImage.apply {
            clear()
            addAll(navArgs.listImage.toList())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagePreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initData() {
        super.initData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    shareViewModel.isPreviewFlow.collect {
                        observePreview(it)
                    }
                }
                launch {
                    viewModel.isDeleteFlow.collect {
                        observeFileDelete(it)
                    }
                }
                launch {
                    listImageViewModel.imageStateFlow.collect {
                        observeImageState(it)
                    }
                }
            }
        }
        viewPageAdapter = ViewPagerAdapter(childFragmentManager, lifecycle).apply {
            navArgs.listImage.forEach {
                addFragment(ImageFragment.newInstance(imagePath = it.path))
            }
        }
        viewPageAdapter?.let {
            binding.viewPager.apply {
                offscreenPageLimit = it.itemCount
                adapter = it
                registerOnPageChangeCallback(callback)
            }
        }
    }

    private fun observePreview(isPreview: Boolean) {
        this.isPreview = isPreview
        binding.groupOption.isGone = isPreview
    }

    private fun observeFileDelete(isDelete: Boolean?) {
        if (isDelete != null && viewPageAdapter != null) {
            if (isDelete) {
                binding.viewLoading.root.isGone = true
                removeImage()
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
                removeImage()
            }
        }
    }

    private fun removeImage() {
        val deletePosition = selectedPosition
        when {
            viewPageAdapter!!.itemCount == 1 -> viewModel.navigateBack()
            selectedPosition == 0 -> binding.viewPager.currentItem = 1
            else -> binding.viewPager.currentItem = selectedPosition - 1
        }
        listImage.removeAt(deletePosition)
        listDelete.add(deletePosition)
        shareViewModel.setRemovePosition(deletePosition)
    }

    override fun initListener() {
        binding.apply {
            imgBack.setOnClickListener {
                viewModel.navigateBack()
            }
            tvRestore.setOnClickListener {
                listImageViewModel.moveImage(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath,
                    listOf(navArgs.listImage[binding.viewPager.currentItem])
                )
            }
            imgPreview.setOnClickListener {
                isPreview = !isPreview
                shareViewModel.setPreview(isPreview)
            }
            imgRotate.setOnClickListener {
                deg += 90f
                shareViewModel.setDeg(deg)
            }
            imgShare.setOnClickListener {
                shareViewModel.shareImage(navArgs.listImage[selectedPosition].path)
            }
            imgDelete.setOnClickListener {
                onDeleteImageListener()
            }
        }
    }

    private fun onDeleteImageListener() {
        val deleteDialog = DialogDelete.newInstance()
        if (!deleteDialog.isShown) {
            deleteDialog.show(childFragmentManager, "")
        }
        deleteDialog.onConfirmListener = {
            binding.viewLoading.root.isVisible = true
            viewModel.deleteImage(navArgs.listImage[selectedPosition].path)
        }
    }

    private val callback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (listDelete.contains(position)) {
                    selectedPosition = position + 1
                    binding.viewPager.currentItem = position + 1
                } else {
                    selectedPosition = position
                    binding.viewPager.currentItem = position
                }
            }
        }
}