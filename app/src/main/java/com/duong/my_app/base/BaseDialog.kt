package com.duong.my_app.base

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.duong.my_app.R

/**
 * Created by Hưng Nguyễn on 24/01/2024
 * Phone: 0335236374
 * Email: nguyenhunghung2806@gmail.com
 */
abstract class BaseDialog : DialogFragment() {

    override fun onStart() {
        super.onStart()
        isCancelable = false
    }

    override fun getTheme(): Int {
        return R.style.RoundedCornersDialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initData()
        initListener()
    }

    open fun initData() {}
    open fun initUI() {}
    open fun initListener() {}

    var isShown = false
        private set

    override fun show(manager: FragmentManager, tag: String?) {
        if (isShown) return
        super.show(manager, tag)
        isShown = true
    }

    override fun onDismiss(dialog: DialogInterface) {
        isShown = false
        super.onDismiss(dialog)
    }
}