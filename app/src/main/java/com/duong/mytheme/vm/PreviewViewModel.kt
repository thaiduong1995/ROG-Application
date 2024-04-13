package com.duong.mytheme.vm

import com.duong.mytheme.base.BaseViewModel
import com.duong.mytheme.data.database.MyThemeRepository
import com.duong.mytheme.ui.fragment.PreviewFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val repository: MyThemeRepository
) : BaseViewModel() {

    val isFirstTimeFlow = repository.isFirstTime

    fun goToMain() {
        navigate(PreviewFragmentDirections.actionPreviewToMain())
    }
}