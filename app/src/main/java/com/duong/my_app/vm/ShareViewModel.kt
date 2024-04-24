package com.duong.my_app.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duong.my_app.data.model.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class ShareViewModel : ViewModel() {

    private val _imageSizeFlow: MutableStateFlow<Int> = MutableStateFlow(0)
    val imageSizeFlow: StateFlow<Int> = _imageSizeFlow

    private val _isPreviewFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isPreviewFlow: StateFlow<Boolean> = _isPreviewFlow

    private val _degFlow: MutableStateFlow<Float> = MutableStateFlow(0f)
    val degFlow: StateFlow<Float> = _degFlow

    private val _shareImageFlow: MutableStateFlow<File?> = MutableStateFlow(null)
    val shareImageFlow: StateFlow<File?> = _shareImageFlow

    private val _removePositionFlow: MutableStateFlow<Int> = MutableStateFlow(-1)
    val removePositionFlow: StateFlow<Int> = _removePositionFlow

    fun setImageSize(imageSize: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _imageSizeFlow.value = imageSize
        }
    }

    fun setPreview(isPreview: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            _isPreviewFlow.value = isPreview
        }
    }

    fun setDeg(deg: Float) {
        viewModelScope.launch(Dispatchers.IO) {
            _degFlow.value = deg
        }
    }

    fun setRemovePosition(position: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _removePositionFlow.value = position
        }
    }

    fun shareImage(filePath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _shareImageFlow.value = File(filePath)
        }
    }

    fun clearImageOption() {
        viewModelScope.launch(Dispatchers.IO) {
            _shareImageFlow.value = null
            _removePositionFlow.value = -1
        }
    }
}