package com.duong.my_app.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.duong.my_app.R
import com.duong.my_app.base.BaseFragment
import com.duong.my_app.databinding.FragmentMainBinding
import com.duong.my_app.ui.adapter.ViewPagerAdapter
import com.duong.my_app.ui.custom.bottombar.BottomNavigation
import com.duong.my_app.utls.Constant
import com.duong.my_app.vm.MainViewModel
import kotlinx.coroutines.launch

class MainFragment : BaseFragment() {

    override val viewModel by viewModels<MainViewModel>()

    private lateinit var binding: FragmentMainBinding
    private var currentTab = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun fetchData(context: Context) {
        super.fetchData(context)
        viewModel.removePreview()
    }

    override fun initData() {
        super.initData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.bundleSaveFlow.collect {
                        observeBundleSave(it)
                    }
                }
            }
        }
        val viewPager = ViewPagerAdapter(childFragmentManager, lifecycle).apply {
            addFragment(ThemeFragment.newInstance(), getString(R.string.theme))
            addFragment(AppManagerFragment.newInstance(), getString(R.string.app_manager))
            addFragment(FolderFragment.newInstance(), getString(R.string.folder))
        }
        binding.viewPager.apply {
            isUserInputEnabled = false
            isSaveEnabled = false
            offscreenPageLimit = viewPager.itemCount
            adapter = viewPager
        }
    }

    private fun observeBundleSave(bundle: Bundle?) {
        bundle?.let {
            binding.viewPager.setCurrentItem(it.getInt(Constant.CURRENT_TAB), false)
        }
    }

    override fun initListener() {
        binding.apply {
            bottomNavView.setOnTabSelectedListener(object : BottomNavigation.OnTabSelectedListener {
                override fun onTabSelected(position: Int, prePosition: Int) {
                    binding.viewPager.setCurrentItem(position, false)
                }

                override fun onTabUnselected(position: Int) {}

                override fun onTabReselected(position: Int) {}

            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.savedInstanceState(
            bundleOf(Constant.CURRENT_TAB to binding.viewPager.currentItem)
        )
        viewModel.clearData()
    }

    companion object {
        private const val TAG = "MainFragment"
    }
}