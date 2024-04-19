package com.duong.my_app.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duong.my_app.data.model.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShareViewModel : ViewModel() {

    private val _listImageSelectedFlow: MutableStateFlow<List<Image>> = MutableStateFlow(listOf())
    val listImageSelectedFlow: StateFlow<List<Image>> = _listImageSelectedFlow

    fun setListImageSelected(listImage: List<Image>) {
        viewModelScope.launch(Dispatchers.IO) {
            _listImageSelectedFlow.value = listImage
        }
    }
}