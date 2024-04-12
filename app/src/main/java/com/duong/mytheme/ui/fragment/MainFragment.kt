package com.duong.mytheme.ui.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.duong.mytheme.R
import com.duong.mytheme.base.BaseFragment
import com.duong.mytheme.base.BaseViewModel
import com.duong.mytheme.data.reponse.VideoState
import com.duong.mytheme.databinding.FragmentMainBinding
import com.duong.mytheme.extension.showSnackBar
import com.duong.mytheme.service.VideoLiveWallpaperService
import com.duong.mytheme.ui.adapter.VideoPreviewAdapter
import com.duong.mytheme.utls.rcv.addItemDecoration
import com.duong.mytheme.vm.MainViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainFragment : BaseFragment() {

    override val viewModel by viewModels<MainViewModel>()

    private lateinit var binding: FragmentMainBinding
    private val videoPreviewAdapter = VideoPreviewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun fetchData(context: Context) {
        super.fetchData(context)
        requestPermission()
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
                resources.getDimensionPixelOffset(R.dimen.size_10),
                resources.getDimensionPixelOffset(R.dimen.size_10)
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
            requestPermission
        }
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            requestPermission.launch(Manifest.permission.READ_MEDIA_VIDEO)
        }
    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            binding.btnRequest.isGone = true
            viewModel.providerListAllVideo()
        } else {
            binding.btnRequest.isVisible = true
        }
    }
}