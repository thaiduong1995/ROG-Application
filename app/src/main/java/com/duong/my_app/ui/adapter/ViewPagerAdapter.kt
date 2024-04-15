package com.duong.my_app.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.duong.my_app.base.BaseFragment

class ViewPagerAdapter(
    manager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(manager, lifecycle) {

    private val listFragment = mutableListOf<BaseFragment>()
    private val listTitle = mutableListOf<String>()
    private val listIcon = mutableListOf<Int>()

    fun addFragment(fragment: BaseFragment, title: String, icon: Int? = null) {
        listFragment.add(fragment)
        listTitle.add(title)
        icon?.let { listIcon.add(it) }
    }

    fun getTitle(position: Int): String {
        return listTitle[position]
    }

    fun getIcon(position: Int): Int? {
        return listIcon.getOrNull(position)
    }

    override fun getItemCount(): Int {
        return listFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return listFragment[position]
    }
}