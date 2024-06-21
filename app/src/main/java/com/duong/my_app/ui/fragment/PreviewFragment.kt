package com.duong.my_app.ui.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.duong.my_app.base.BaseFragment
import com.duong.my_app.data.reponse.ProgressBarState
import com.duong.my_app.data.reponse.VideoState
import com.duong.my_app.databinding.FragmentPreviewBinding
import com.duong.my_app.extension.hideStatusBar
import com.duong.my_app.extension.showStatusBar
import com.duong.my_app.vm.PreviewViewModel
import com.duong.my_app.vm.SharedViewModel
import kotlinx.coroutines.launch


class PreviewFragment : BaseFragment() {

    override val viewModel by viewModels<PreviewViewModel>()
    override val TAG = "PreviewFragment"

    private var _binding: FragmentPreviewBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override fun fetchData(context: Context) {
        super.fetchData(context)

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            ) == PackageManager.PERMISSION_GRANTED -> {
                sharedViewModel.providerListAllVideo()
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED -> {
                sharedViewModel.providerListAllVideo()
            }

            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                sharedViewModel.providerListAllVideo()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initData() {
        super.initData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.firstInstall.collect {
                        observeFirstInstall(it)
                    }
                }
            }
        }
    }

    private fun observeFirstInstall(firstInstall: Int) {
        when (firstInstall) {
            0 -> {
                binding.videoPreview.apply {
                    isVisible = true
                    setVideoPath("content://com.duong.my_app/video_preview.mp4")
                    start()
                    setOnCompletionListener {
                        viewModel.goToMain()
                    }
                }
            }

            2 -> {
                binding.imgLogo.isVisible = true
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
        viewModel.clearData()
    }
}