package com.duong.rog.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.duong.rog.base.BaseFragment
import com.duong.rog.data.model.Image
import com.duong.rog.databinding.FragmentImagePreviewBinding
import com.duong.rog.extension.getCurrentPosition
import com.duong.rog.ui.adapter.ImagePreviewAdapter
import com.duong.rog.ui.dialog.DialogDelete
import com.duong.rog.utls.rcv.CustomScrollListener
import com.duong.rog.vm.ImagePreviewViewModel
import com.duong.rog.vm.SharedViewModel
import kotlinx.coroutines.launch
import java.io.File

class ImagePreviewFragment : BaseFragment() {

    override val viewModel by viewModels<ImagePreviewViewModel>()
    override val TAG = "VideoFragment"

    private var _binding: FragmentImagePreviewBinding? = null
    private val binding get() = _binding!!
    private val navArgs by navArgs<ImagePreviewFragmentArgs>()

    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private var imagePreviewAdapter = ImagePreviewAdapter()
    private val imageList = mutableListOf<Image>()
    private var isPreview = false

    override fun fetchData(context: Context) {
        super.fetchData(context)
        imageList.apply {
            clear()
            addAll(navArgs.imageList.toList())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImagePreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initData() {
        super.initData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    sharedViewModel.isPreviewFlow.collect {
                        observePreview(it)
                    }
                }
            }
        }
        imagePreviewAdapter.setData(imageList)
    }

    private fun observePreview(isPreview: Boolean) {
        this.isPreview = isPreview
        binding.groupPreview.isGone = isPreview
    }

    override fun initUi() {
        binding.viewPager.apply {
            adapter = imagePreviewAdapter
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val snapHelper: SnapHelper = PagerSnapHelper()
            this.layoutManager = layoutManager
            snapHelper.attachToRecyclerView(this)
            scrollToPosition(navArgs.position)
        }
    }

    override fun initListener() {
        binding.apply {
            imgBack.setOnClickListener {
                viewModel.navigateBack()
            }
            imgShare.setOnClickListener {  }
            imgDelete.setOnClickListener {
                onDeleteListener()
            }
            viewPager.addOnScrollListener(object : CustomScrollListener() {
                override fun onStartScroll() {
                    //initViewPaper()
                }
            })
        }
        imagePreviewAdapter.onClickItemListener = { position, view ->
            isPreview = !isPreview
            sharedViewModel.setPreview(isPreview)
        }
    }

    private fun onDeleteListener() {
        val deleteDialog = DialogDelete.newInstance()
        if (!deleteDialog.isShown) {
            deleteDialog.show(childFragmentManager, DialogDelete.TAG)
        }
        deleteDialog.onConfirmListener = {
            val position = binding.viewPager.getCurrentPosition()
            File(imageList[position].path).delete()
            imagePreviewAdapter.removeItem(position)
            sharedViewModel.deleteImage(position)
        }
    }


    companion object {

        @JvmStatic
        fun newInstance() = MediaFragment().apply {
            arguments = bundleOf()
        }
    }
}