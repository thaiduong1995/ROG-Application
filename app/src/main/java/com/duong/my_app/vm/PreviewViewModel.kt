package com.duong.my_app.vm

import androidx.lifecycle.viewModelScope
import com.duong.my_app.base.BaseViewModel
import com.duong.my_app.data.database.MyThemeRepository
import com.duong.my_app.ui.fragment.PreviewFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val repository: MyThemeRepository
) : BaseViewModel() {

    override val TAG = "PreviewViewModel"

    val firstInstall = repository.firstInstall

    fun goToMain() {
        viewModelScope.launch(Dispatchers.IO) {
            navigate(PreviewFragmentDirections.actionPreviewToMain())
        }
    }

    override fun clearData() {
        viewModelScope.launch(Dispatchers.IO) {
            super.clearData()
        }
    }
}