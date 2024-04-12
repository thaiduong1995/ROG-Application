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
import com.duong.mytheme.base.BaseFragment
import com.duong.mytheme.databinding.FragmentPreviewBinding
import com.duong.mytheme.vm.PreviewViewModel


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
        activity?.window?.apply {
            when {
                Build.VERSION.SDK_INT in 21..29 -> {
                    setFlags(
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    )
                }

                Build.VERSION.SDK_INT >= 30 -> {
                    val windowInsetsController =
                        WindowCompat.getInsetsController(this, this.decorView)
                    // Configure the behavior of the hidden system bars.
                    windowInsetsController.systemBarsBehavior =
                        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

                    // Add a listener to update the behavior of the toggle fullscreen button when
                    // the system bars are hidden or revealed.
                    decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
                        // You can hide the caption bar even when the other system bars are visible.
                        // To account for this, explicitly check the visibility of navigationBars()
                        // and statusBars() rather than checking the visibility of systemBars().
                        if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
                            || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())
                        ) {
                            // Hide both the status bar and the navigation bar.
                            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
                        }
                        view.onApplyWindowInsets(windowInsets)
                    }
                }
            }
        }
    }
}