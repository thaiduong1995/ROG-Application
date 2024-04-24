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
    private var listId: List<Long>? = null

    fun addFragment(fragment: BaseFragment) {
        listFragment.add(fragment)
    }

    override fun getItemCount(): Int {
        return listFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return listFragment[position]
    }

    fun remove(index: Int) {
        listFragment.removeAt(index)
        notifyItemRemoved(index)
        notifyItemChanged(index, itemCount)
        updateItemIds()
    }

    private fun updateItemIds() {
        listId = listFragment.map { it.hashCode().toLong() }
    }

    override fun getItemId(position: Int): Long {
        return listFragment[position].hashCode().toLong()
    }

    override fun containsItem(itemId: Long): Boolean {
        return listFragment.find { it.hashCode().toLong() == itemId } != null
    }
}