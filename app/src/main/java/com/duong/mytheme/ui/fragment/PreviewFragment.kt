package com.duong.mytheme.ui.fragment

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.MediaController
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.duong.mytheme.base.BaseFragment
import com.duong.mytheme.data.reponse.NavigationCommand
import com.duong.mytheme.databinding.FragmentPreviewBinding
import com.duong.mytheme.extension.hideStatusBar
import com.duong.mytheme.extension.showStatusBar
import com.duong.mytheme.vm.PreviewViewModel
import kotlinx.coroutines.flow.collect
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
        binding.videoPreview.apply {
            setVideoPath("content://com.duong.mytheme/video_preview.mp4")
            start()
            setOnCompletionListener {
                viewModel.goToMain()
            }
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