package com.duong.my_app.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.duong.my_app.R
import com.duong.my_app.base.BaseFragment
import com.duong.my_app.databinding.FragmentMainBinding
import com.duong.my_app.ui.adapter.ViewPagerAdapter
import com.duong.my_app.ui.custom.bottombar.BottomNavigation
import com.duong.my_app.vm.MainViewModel

class MainFragment : BaseFragment() {

    override val viewModel by viewModels<MainViewModel>()

    private lateinit var binding: FragmentMainBinding

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

    override fun initListener() {
        binding.apply {
            bottomNavView.setOnTabSelectedListener(object : BottomNavigation.OnTabSelectedListener {
                override fun onTabSelected(position: Int, prePosition: Int) {
                    binding.viewPager.setCurrentItem(position, false)
                }

                override fun onTabUnselected(position: Int) { }

                override fun onTabReselected(position: Int) { }

            })
        }
    }
}