package com.duong.rog.base

import android.content.res.Resources
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.duong.rog.R
import kotlin.math.roundToInt

abstract class BaseDialog : DialogFragment() {

    open var isShow = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        initData()
        initListener()
    }

    open fun initData() {}
    open fun initUi() {}
    open fun initListener() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isShow = isAdded || isVisible
    }

    override fun onStart() {
        super.onStart()
        isCancelable = false
    }

    override fun getTheme(): Int {
        return R.style.RoundedCornersDialog
    }

    open fun setSizeWidth(widthPercentage: Int) {
        val newWidth = widthPercentage.div(100f)
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * newWidth
        val percentHeight = ConstraintLayout.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(percentWidth.roundToInt(), percentHeight)
    }
}