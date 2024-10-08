package com.duong.rog.ui.custom.bottombar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.duong.rog.R
import com.duong.rog.databinding.ViewBottomNavigationBinding

/**
 * Created by Hưng Nguyễn on 05/12/2023
 * Phone: 0335236374
 * Email: nguyenhunghung2806@gmail.com
 */
class BottomNavigation @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    styleDef: Int = 0
) : ConstraintLayout(context, attrs, styleDef) {

    private val binding: ViewBottomNavigationBinding =
        ViewBottomNavigationBinding.inflate(LayoutInflater.from(context), this, true)

    private var onTabSelectedListener: OnTabSelectedListener? = null
    private var bottomItemLayouts = mutableListOf<BottomItemLayout>()
    private var currentItemPosition = 0
        private set

    fun setOnTabSelectedListener(onTabSelectedListener: OnTabSelectedListener?) {
        this.onTabSelectedListener = onTabSelectedListener
    }

    fun setCurrentItemPosition(position: Int) {
        for (i in bottomItemLayouts.indices) {
            bottomItemLayouts[i].isSelected = i == position
            currentItemPosition = position
        }
    }

    private fun setupWithViewpager() {
        bottomItemLayouts[currentItemPosition].isSelected = true
        bottomItemLayouts.onEach { bottom ->
            bottom.setOnClickListener {
                val position = bottom.tabPosition
                if (position == currentItemPosition) {
                    onTabSelectedListener?.onTabReselected(position)
                } else {
                    onTabSelectedListener?.onTabSelected(position, currentItemPosition)
                    bottom.isSelected = true
                    onTabSelectedListener?.onTabUnselected(currentItemPosition)
                    bottomItemLayouts[currentItemPosition].isSelected = false
                    currentItemPosition = position
                }
            }
        }
    }

    init {
        binding.tabTheme.apply {
            setBottomItem(
                BottomItem(
                    icon = R.drawable.ic_theme,
                    title = context.getString(R.string.theme)
                )
            )
            setTabPosition(bottomItemLayouts.size)
            bottomItemLayouts.add(this)
        }

        binding.tabAppManager.apply {
            setBottomItem(
                BottomItem(
                    icon = R.drawable.ic_app_manager,
                    title = context.getString(R.string.app_manager)
                )
            )
            setTabPosition(bottomItemLayouts.size)
            bottomItemLayouts.add(this)
        }

        binding.tabFolder.apply {
            setBottomItem(
                BottomItem(
                    icon = R.drawable.ic_folder,
                    title = context.getString(R.string.media)
                )
            )
            setTabPosition(bottomItemLayouts.size)
            bottomItemLayouts.add(this)
        }

        setupWithViewpager()
    }

    interface OnTabSelectedListener {
        fun onTabSelected(position: Int, prePosition: Int)
        fun onTabUnselected(position: Int)
        fun onTabReselected(position: Int)
    }
}