package com.duong.my_app.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.duong.my_app.data.model.Image
import com.duong.my_app.data.reponse.NavigationCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _navigationFlow: MutableStateFlow<NavigationCommand?> = MutableStateFlow(null)
    val navigationFlow: StateFlow<NavigationCommand?> = _navigationFlow

    private val _bundleSaveFlow: MutableStateFlow<Bundle?> = MutableStateFlow(null)
    val bundleSaveFlow: StateFlow<Bundle?> = _bundleSaveFlow

    private val _listImageSelectedFlow: MutableStateFlow<List<Image>> = MutableStateFlow(listOf())
    val listImageSelectedFlow: StateFlow<List<Image>> = _listImageSelectedFlow

    fun navigate(navDirections: NavDirections) {
        viewModelScope.launch(Dispatchers.IO) {
            _navigationFlow.value = NavigationCommand.ToDirection(navDirections)
        }
    }

    fun navigateBack() {
        viewModelScope.launch(Dispatchers.IO) {
            _navigationFlow.value = NavigationCommand.ToBack
        }
    }

    open fun clearData() {
        viewModelScope.launch(Dispatchers.IO) {
            _navigationFlow.value = null
        }
    }

    fun savedInstanceState(bundle: Bundle) {
        viewModelScope.launch(Dispatchers.IO) {
            _bundleSaveFlow.value = bundle
        }
    }

    fun setListImageSelected(listImage: List<Image>) {
        viewModelScope.launch(Dispatchers.IO) {
            _listImageSelectedFlow.value = listImage
        }
    }
}