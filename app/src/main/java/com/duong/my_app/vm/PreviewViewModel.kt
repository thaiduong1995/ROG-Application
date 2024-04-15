package com.duong.my_app.vm

import com.duong.my_app.base.BaseViewModel
import com.duong.my_app.data.database.MyThemeRepository
import com.duong.my_app.ui.fragment.PreviewFragmentDirections
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