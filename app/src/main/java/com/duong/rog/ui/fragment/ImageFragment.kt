package com.duong.rog.ui.fragment

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.duong.rog.R
import com.duong.rog.base.BaseFragment
import com.duong.rog.data.model.Image
import com.duong.rog.data.reponse.ImageState
import com.duong.rog.databinding.FragmentImageBinding
import com.duong.rog.ui.adapter.ImageAdapter
import com.duong.rog.utls.rcv.addItemDecoration
import com.duong.rog.vm.ImageViewModel
import com.duong.rog.vm.SharedViewModel
import kotlinx.coroutines.launch

class ImageFragment : BaseFragment() {

    override val viewModel by viewModels<ImageViewModel>()
    override val TAG = "ImageFragment"

    private var _binding: FragmentImageBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private val imageAdapter = ImageAdapter()
    private val imageList = mutableListOf<Image>()

    override fun fetchData(context: Context) {
        super.fetchData(context)

        requestPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initData() {
        super.initData()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    sharedViewModel.bundleSaveFlow.collect {
                        observeBundleSave(it)
                    }
                }
                launch {
                    sharedViewModel.imageListFlow.collect {
                        observeImageList(it)
                    }
                }
            }
        }
    }

    private fun observeBundleSave(bundleList: List<Bundle>) {

    }

    private fun observeImageList(imageState: ImageState) {
        Log.d("ProgressBarState", "$TAG - $imageState")
//        when (imageState) {
//            is ImageState.Error -> {}
//            ImageState.Idle -> {}
//            ImageState.Prepare -> {
//                requestPermission()
//            }
//            ImageState.Loading -> {
//                sharedViewModel.setProgressBarState(ProgressBarState.LoadingData)
//                imageList.clear()
//            }
//
//            is ImageState.Success -> {
//                sharedViewModel.setProgressBarState(ProgressBarState.Idle)
//                imageAdapter.setData(imageState.imageList)
//                imageList.addAll(imageState.imageList)
//            }
//
//        }
    }


    override fun initUi() {
        binding.rcvImage.apply {
            adapter = imageAdapter
            addItemDecoration(
                resources.getDimensionPixelOffset(R.dimen.size_10),
                resources.getDimensionPixelOffset(R.dimen.size_10)
            )
        }
    }

    override fun initListener() {
        imageAdapter.onClickItemListener = { image, position ->
            viewModel.goToImagePreview(imageList.toTypedArray(), position)
        }
    }

    private fun requestPermission() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> requestPermission.launch(
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            )

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> requestPermission.launch(
                Manifest.permission.READ_MEDIA_IMAGES
            )

            else -> requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            when {
                isGranted -> {
                    sharedViewModel.providerListAllImage()
                    onDismissPermissionDialog()
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE && !shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                ) -> alertDialog?.show()
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_MEDIA_IMAGES
                ) -> alertDialog?.show()
                Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && !shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) -> alertDialog?.show()
            }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.clearData()
    }

    companion object {

        @JvmStatic
        fun newInstance() = ImageFragment().apply {
            arguments = bundleOf()
        }
    }
}