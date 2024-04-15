package com.duong.my_app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.duong.my_app.base.BaseFragment
import com.duong.my_app.databinding.FragmentPreviewBinding
import com.duong.my_app.extension.hideStatusBar
import com.duong.my_app.extension.showStatusBar
import com.duong.my_app.vm.PreviewViewModel
import kotlinx.coroutines.launch


class PreviewFragment : BaseFragment() {

    override val viewModel by viewModels<PreviewViewModel>()

    private lateinit var binding: FragmentPreviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initData() {
        super.initData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.isFirstTimeFlow.collect {
                        observeIsFirstTime(it)
                    }
                }
            }
        }
    }

    private fun observeIsFirstTime(isFirstTime: Boolean) {
        if (isFirstTime) {
            binding.videoPreview.apply {
                isVisible = true
                setVideoPath("content://com.duong.my_app/video_preview.mp4")
                start()
                setOnCompletionListener {
                    viewModel.goToMain()
                }
            }
        } else {
            binding.imgLogo.isVisible = true
            viewModel.goToMain()
        }
    }

    override fun initUi() {
        activity?.hideStatusBar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.showStatusBar()
    }
}