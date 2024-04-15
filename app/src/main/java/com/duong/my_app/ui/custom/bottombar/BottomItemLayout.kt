package com.duong.my_app.ui.custom.bottombar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.duong.my_app.R
import com.duong.my_app.databinding.ItemBottomNavigationViewBinding

class BottomItemLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    styleDef: Int = 0
) : ConstraintLayout(context, attrs, styleDef) {

    private val binding: ItemBottomNavigationViewBinding =
        ItemBottomNavigationViewBinding.inflate(LayoutInflater.from(context), this, true)

    var tabPosition = -1
        private set

    fun setTabPosition(position: Int) {
        tabPosition = position
        if (position == 0) {
            isSelected = true
        }
    }

    fun setBottomItem(bottomItem: BottomItem) {
        binding.imgThumb.setImageResource(bottomItem.icon)
        binding.tvTitle.text = bottomItem.title
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        binding.apply {
            imgThumb.isSelected = isSelected
            tvTitle.setTextColor(if (isSelected) ContextCompat.getColor(context, R.color.green4DEC9B) else ContextCompat.getColor(context, R.color.white))
        }
    }

    init {
        isSelected = false
    }
}