package com.duong.my_app.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duong.my_app.data.model.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShareViewModel : ViewModel() {

    private val _imageSizeFlow: MutableStateFlow<Int> = MutableStateFlow(0)
    val imageSizeFlow: StateFlow<Int> = _imageSizeFlow

    fun setImageSize(imageSize: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _imageSizeFlow.value = imageSize
        }
    }
}