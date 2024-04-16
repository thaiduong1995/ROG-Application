package com.duong.my_app.ui.fragment

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.duong.my_app.R
import com.duong.my_app.base.BaseFragment
import com.duong.my_app.databinding.FragmentImageBinding
import com.duong.my_app.vm.ImageViewModel

class ImageFragment : BaseFragment() {

    override val viewModel by viewModels<ImageViewModel>()

    private lateinit var binding: FragmentImageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = ImageFragment().apply {
            arguments = bundleOf()
        }
    }
}