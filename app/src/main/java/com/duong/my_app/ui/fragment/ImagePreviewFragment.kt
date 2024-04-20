package com.duong.my_app.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.duong.my_app.base.BaseFragment
import com.duong.my_app.databinding.FragmentImagePreviewBinding
import com.duong.my_app.vm.FolderDetailViewModel

class ImagePreviewFragment : BaseFragment() {

    override val viewModel by viewModels<FolderDetailViewModel>()

    private lateinit var binding: FragmentImagePreviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImagePreviewBinding.inflate(inflater, container, false)
        return binding.root
    }
}