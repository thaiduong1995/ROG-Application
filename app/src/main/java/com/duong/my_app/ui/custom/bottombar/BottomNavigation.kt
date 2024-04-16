package com.duong.my_app.ui.custom.bottombar

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.duong.my_app.R
import com.duong.my_app.databinding.WaveBottomNavigationViewBinding

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

    private val binding: WaveBottomNavigationViewBinding =
        WaveBottomNavigationViewBinding.inflate(LayoutInflater.from(context), this, true)

    private var onTabSelectedListener: OnTabSelectedListener? = null
    private var bottomItemLayouts = mutableListOf<BottomItemLayout>()
    var currentItemPosition = 0
        private set

    fun setOnTabSelectedListener(onTabSelectedListener: OnTabSelectedListener?) {
        this.onTabSelectedListener = onTabSelectedListener
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
                    title = context.getString(R.string.folder)
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