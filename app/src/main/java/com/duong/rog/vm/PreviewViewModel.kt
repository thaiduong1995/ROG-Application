package com.duong.rog.vm

import androidx.lifecycle.viewModelScope
import com.duong.rog.base.BaseViewModel
import com.duong.rog.data.database.ROGRepository
import com.duong.rog.ui.fragment.PreviewFragmentDirections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val repository: ROGRepository
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