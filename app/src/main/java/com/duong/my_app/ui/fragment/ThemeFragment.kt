package com.duong.my_app.ui.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.duong.my_app.R
import com.duong.my_app.base.BaseFragment
import com.duong.my_app.data.model.Video
import com.duong.my_app.data.reponse.ProgressBarState
import com.duong.my_app.data.reponse.VideoState
import com.duong.my_app.databinding.FragmentThemeBinding
import com.duong.my_app.extension.showSnackBar
import com.duong.my_app.service.VideoLiveWallpaperService
import com.duong.my_app.ui.adapter.VideoPreviewAdapter
import com.duong.my_app.utls.rcv.addItemDecoration
import com.duong.my_app.utls.rcv.parcelableArray
import com.duong.my_app.vm.SharedViewModel
import com.duong.my_app.vm.ThemeViewModel
import kotlinx.coroutines.launch

class ThemeFragment : BaseFragment() {

    override val viewModel by viewModels<ThemeViewModel>()
    override val TAG = "ThemeFragment"

    private var _binding: FragmentThemeBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private val videoPreviewAdapter = VideoPreviewAdapter()

    override fun fetchData(context: Context) {
        super.fetchData(context)

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            ) != PackageManager.PERMISSION_GRANTED -> {
                requestPermission.launch(Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_VIDEO
            ) != PackageManager.PERMISSION_GRANTED -> {
                requestPermission.launch(Manifest.permission.READ_MEDIA_VIDEO)
            }

            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED -> {
                requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentThemeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initData() {
        super.initData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    launch {
                        sharedViewModel.videoListFlow.collect {
                            observeVideoList(it)
                        }
                    }
                }
            }
        }
    }

    private fun observeVideoList(videoState: VideoState) {
        Log.d("ProgressBarState", "$TAG - $videoState")
        when (videoState) {
            is VideoState.Error -> {}
            VideoState.IDLE -> {}
            VideoState.Loading -> {
                sharedViewModel.setProgressBarState(ProgressBarState.LoadingData)
            }

            is VideoState.Success -> {
                sharedViewModel.setProgressBarState(ProgressBarState.Idle)
                videoPreviewAdapter.setData(videoState.videoList)
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

    override fun onDismissPermissionDialog() {
        super.onDismissPermissionDialog()
        binding.btnRequest.isVisible = true
    }

    private fun requestPermission() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> requestPermission.launch(
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            )

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> requestPermission.launch(
                Manifest.permission.READ_MEDIA_VIDEO
            )

            else -> requestPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                binding.btnRequest.isGone = true
                if (alertDialog?.isShowing == true) {
                    alertDialog?.dismiss()
                }
                sharedViewModel.providerListAllVideo()
            } else {
                binding.btnRequest.isVisible = true
                when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE && !shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                    ) -> alertDialog?.show()

                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_MEDIA_VIDEO
                    ) -> alertDialog?.show()

                    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU && !shouldShowRequestPermissionRationale(
                        Manifest.permission.READ_EXTERNAL_STORAGE
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