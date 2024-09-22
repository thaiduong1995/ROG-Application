package com.duong.rog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.duong.rog.base.BaseFragment
import com.duong.rog.databinding.FragmentMediaBinding
import com.duong.rog.ui.adapter.ViewPagerAdapter
import com.duong.rog.vm.MediaViewModel
import com.google.android.material.tabs.TabLayoutMediator

class MediaFragment : BaseFragment() {

    override val viewModel by viewModels<MediaViewModel>()
    override val TAG = "MediaFragment"

    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun initData() {
        super.initData()

        val viewPager = ViewPagerAdapter(childFragmentManager, lifecycle).apply {
            addFragment(ImageFragment.newInstance())
            addFragment(ImagePreviewFragment.newInstance())
        }
        binding.viewPager.apply {
            isUserInputEnabled = false
            isSaveEnabled = false
            offscreenPageLimit = viewPager.itemCount
            adapter = viewPager
        }
    }

    override fun initUi() {
        val tabTitles = listOf("Image", "Video")
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    companion object {

        @JvmStatic
        fun newInstance() = MediaFragment().apply {
            arguments = bundleOf()
        }
    }
}