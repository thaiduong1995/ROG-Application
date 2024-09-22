package com.duong.rog.vm

import androidx.lifecycle.viewModelScope
import com.duong.rog.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImagePreviewViewModel @Inject constructor(): BaseViewModel() {

    override val TAG = "ImagePreviewViewModel"

    private val _isDeleteFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isDeleteFlow: StateFlow<Boolean?> = _isDeleteFlow


    override fun clearData() {
        viewModelScope.launch(Dispatchers.IO) {
            _isDeleteFlow.value = null
        }
    }
}