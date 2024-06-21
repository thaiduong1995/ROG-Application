package com.duong.my_app.ui.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.duong.my_app.base.BaseFragment
import com.duong.my_app.databinding.FragmentMainBinding
import com.duong.my_app.ui.adapter.ViewPagerAdapter
import com.duong.my_app.ui.custom.bottombar.BottomNavigation
import com.duong.my_app.utls.Constant
import com.duong.my_app.vm.MainViewModel
import com.duong.my_app.vm.SharedViewModel
import kotlinx.coroutines.launch

class MainFragment : BaseFragment() {

    override val viewModel by viewModels<MainViewModel>()
    override val TAG = "MainFragment"

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun fetchData(context: Context) {
        super.fetchData(context)
        viewModel.removePreview(2)
    }

    override fun initData() {
        super.initData()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    sharedViewModel.bundleSaveFlow.collect {
                        observeBundleSave(it)
                    }
                }
            }
        }
        val viewPager = ViewPagerAdapter(childFragmentManager, lifecycle).apply {
            addFragment(ThemeFragment.newInstance())
            addFragment(AppManagerFragment.newInstance())
            addFragment(MediaFragment.newInstance())
        }
        binding.viewPager.apply {
            isUserInputEnabled = false
            isSaveEnabled = false
            offscreenPageLimit = viewPager.itemCount
            adapter = viewPager
        }
    }

    private fun observeBundleSave(bundleList: List<Bundle?>) {
        bundleList.forEach { bundle ->
            Log.d(TAG, "observeBundleSave: $bundle")
            if (bundle?.containsKey(Constant.CURRENT_TAB) == true) {
                binding.viewPager.setCurrentItem(bundle.getInt(Constant.CURRENT_TAB), false)
                binding.bottomNavView.setCurrentItemPosition(bundle.getInt(Constant.CURRENT_TAB))
            }
        }
    }

    override fun initListener() {
        binding.bottomNavView.setOnTabSelectedListener(object :
            BottomNavigation.OnTabSelectedListener {
            override fun onTabSelected(position: Int, prePosition: Int) {
                binding.viewPager.setCurrentItem(position, false)
                //if (position == 2) {
                 //   sharedViewModel.goToImage()
                //}
            }

            override fun onTabUnselected(position: Int) {}

            override fun onTabReselected(position: Int) {}

        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        sharedViewModel.savedInstanceState(
            bundleOf(Constant.CURRENT_TAB to binding.viewPager.currentItem)
        )
        viewModel.clearData()
    }
}