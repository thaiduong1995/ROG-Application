package com.duong.mytheme.vm

import com.duong.mytheme.base.BaseViewModel
import com.duong.mytheme.ui.fragment.PreviewFragmentDirections
import javax.inject.Inject

class PreviewViewModel @Inject constructor() : BaseViewModel() {

    fun goToMain() {
        navigate(PreviewFragmentDirections.actionPreviewToMain())
    }
}