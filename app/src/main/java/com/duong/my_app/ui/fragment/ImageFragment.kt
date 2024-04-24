package com.duong.my_app.ui.fragment

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.duong.my_app.BuildConfig
import com.duong.my_app.R
import com.duong.my_app.base.BaseFragment
import com.duong.my_app.databinding.FragmentImageBinding
import com.duong.my_app.vm.ImageViewModel
import com.duong.my_app.vm.ShareViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File

class ImageFragment : BaseFragment() {


    override val viewModel by viewModels<ImageViewModel>()

    private lateinit var binding: FragmentImageBinding
    private val shareViewModel by activityViewModels<ShareViewModel>()
    private var isPreview = false
    private var deg = 0f
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initData() {
        super.initData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    shareViewModel.degFlow.collect {
                        observeDeg(it)
                    }
                }
                launch {
                    shareViewModel.isPreviewFlow.collect {
                        observePreview(it)
                    }
                }
                launch {
                    shareViewModel.shareImageFlow.collect {
                        observeImageShare(it)
                    }
                }
            }
        }
    }

    private fun observeDeg(deg: Float) {
        this.deg = deg
        binding.photoView.setRotationTo(deg)
    }

    private fun observePreview(isPreview: Boolean) {
        this.isPreview = isPreview
    }

    private fun observeImageShare(imageFile: File?) {
        if (context != null && imageFile != null) {
            val uri = FileProvider.getUriForFile(
                requireContext(),
                BuildConfig.APPLICATION_ID + ".provider",
                imageFile
            )
            val intent = Intent(Intent.ACTION_SEND)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setType("image/*")
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun initUi() {
        with(binding.photoView) {
            Glide.with(context).load(arguments?.getString(IMAGE_PATH)).into(this)
        }
    }

    override fun initListener() {
        binding.apply {
            photoView.setOnClickListener {
                isPreview = !isPreview
                shareViewModel.setPreview(isPreview)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        shareViewModel.clearImageOption()
    }

    companion object {

        private const val IMAGE_PATH = "IMAGE_PATH"

        @JvmStatic
        fun newInstance(imagePath: String) = ImageFragment().apply {
            arguments = bundleOf(IMAGE_PATH to imagePath)
        }
    }
}