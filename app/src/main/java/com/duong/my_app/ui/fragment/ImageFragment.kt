package com.duong.my_app.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.duong.my_app.R
import com.duong.my_app.base.BaseFragment
import com.duong.my_app.data.model.Image
import com.duong.my_app.data.reponse.ImageState
import com.duong.my_app.databinding.FragmentImageBinding
import com.duong.my_app.ui.adapter.ImageAdapter
import com.duong.my_app.utls.rcv.addItemDecoration
import com.duong.my_app.vm.ImageViewModel
import kotlinx.coroutines.launch

class ImageFragment : BaseFragment() {

    override val viewModel by viewModels<ImageViewModel>()

    private lateinit var binding: FragmentImageBinding
    private val imageAdapter = ImageAdapter()
    private val listImageSelected = mutableListOf<Image>()
    private var isSelectAll = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun fetchData(context: Context) {
        super.fetchData(context)
        viewModel.getAllShownImagesPath()
    }

    override fun initData() {
        super.initData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.listImageFlow.collect {
                        observeListImage(it)
                    }
                }
            }
        }
    }

    private fun observeListImage(imageState: ImageState) {
        when (imageState) {
            is ImageState.Error -> {
                Log.e(TAG, "load list image failed ${imageState.message}")
            }
            ImageState.IDLE -> {}
            ImageState.Loading -> binding.progressBar.isVisible = true
            is ImageState.Success -> {
                binding.progressBar.isGone = true
                imageAdapter.setData(imageState.listImage)
            }
        }
    }

    override fun initUi() {
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
                viewModel.setListImageSelected(listImageSelected)
                viewModel.navigateBack()
            }
            tvSelectAll.setOnClickListener {
                onSelectAllListener()
            }
            imgSelectAll.setOnClickListener {
                onSelectAllListener()
            }
        }
        imageAdapter.apply {
            onSelectItemListener = { image, position ->
                if (image.isSelected && !listImageSelected.contains(image)) {
                    listImageSelected.add(image)
                }
            }
            onSelectAllListener = { listImage ->
                if (listImage.first().isSelected) {
                    listImageSelected.apply {
                        clear()
                        addAll(listImage)
                    }
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

    companion object {

        @JvmStatic
        fun newInstance() = ImageFragment().apply {
            arguments = bundleOf()
        }
    }
}