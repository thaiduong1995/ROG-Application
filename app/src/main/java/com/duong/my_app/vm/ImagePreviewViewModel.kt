package com.duong.my_app.vm

import androidx.lifecycle.viewModelScope
import com.duong.my_app.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

class ImagePreviewViewModel @Inject constructor(): BaseViewModel() {

    private val _isDeleteFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val isDeleteFlow: StateFlow<Boolean?> = _isDeleteFlow

    fun deleteImage(filePath: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isDeleteFlow.value = File(filePath).delete()
        }
    }

    override fun clearData() {
        viewModelScope.launch(Dispatchers.IO) {
            _isDeleteFlow.value = null
        }
    }
}