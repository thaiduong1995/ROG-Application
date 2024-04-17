package com.duong.my_app.ui.fragment

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.duong.my_app.R
import com.duong.my_app.base.BaseFragment
import com.duong.my_app.data.reponse.VideoState
import com.duong.my_app.databinding.FragmentThemeBinding
import com.duong.my_app.extension.showSnackBar
import com.duong.my_app.service.VideoLiveWallpaperService
import com.duong.my_app.ui.adapter.VideoPreviewAdapter
import com.duong.my_app.utls.rcv.addItemDecoration
import com.duong.my_app.vm.ThemeViewModel
import kotlinx.coroutines.launch

class ThemeFragment : BaseFragment() {

    override val viewModel by viewModels<ThemeViewModel>()

    private lateinit var binding: FragmentThemeBinding
    private val videoPreviewAdapter = VideoPreviewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentThemeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initData() {
        super.initData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.listVideoFlow.collect {
                        observeListVideo(it)
                    }
                }
            }
        }
    }

    private fun observeListVideo(videoState: VideoState) {
        when (videoState) {
            is VideoState.Error -> {
                binding.loading.isGone = true
                activity?.showSnackBar(binding.rcvVideo, getString(R.string.common_error))
            }

            VideoState.IDLE -> {}
            VideoState.Loading -> {
                binding.loading.isVisible = true
            }

            is VideoState.Success -> {
                binding.loading.isGone = true
                videoPreviewAdapter.setData(videoState.listVideo)
            }
        }
    }

    override fun initUi() {
        binding.rcvVideo.apply {
            adapter = videoPreviewAdapter
            addItemDecoration(
                resources.getDimensionPixelOffset(R.dimen.size_5),
                resources.getDimensionPixelOffset(R.dimen.size_5)
            )
        }
    }

    override fun initListener() {
        videoPreviewAdapter.onClickItemListener = { video ->
            activity?.let { act ->
                act.openFileOutput("video_live_wallpaper_file_path", Context.MODE_PRIVATE)
                    .use { it.write(video.path.toByteArray()) }
                VideoLiveWallpaperService.apply {
                    muteMusic(act)
                    setToWallPaper(act)
                }
            }
        }
        binding.btnRequest.setOnClickListener {
            requestPermission()

        }
    }

    override fun onStart() {
        super.onStart()
        requestPermission()
    }

    override fun onDismissPermissionDialog() {
        super.onDismissPermissionDialog()
        binding.btnRequest.isVisible = true
    }

    private fun requestPermission() {
        activity?.let { act ->
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                requestPermission.launch(Manifest.permission.READ_MEDIA_VIDEO)
            }
        }
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                binding.btnRequest.isGone = true
                if (alertDialog?.isShowing == true) {
                    alertDialog?.dismiss()
                }
                viewModel.providerListAllVideo()
            } else {
                binding.btnRequest.isVisible = true
                when {
                    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && !shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) -> alertDialog?.show()

                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_MEDIA_VIDEO
                    ) -> alertDialog?.show()
                }
            }
        }

    companion object {

        @JvmStatic
        fun newInstance() = ThemeFragment().apply {
            arguments = bundleOf()
        }
    }
}