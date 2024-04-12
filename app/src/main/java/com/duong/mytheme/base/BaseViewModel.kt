package com.duong.mytheme.base

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.duong.mytheme.data.reponse.NavigationCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel: ViewModel() {

    private val _navigationFlow: MutableStateFlow<NavigationCommand?> = MutableStateFlow(null)
    val navigationFlow: StateFlow<NavigationCommand?> = _navigationFlow

    private val _bundleFlow: MutableStateFlow<Bundle?> = MutableStateFlow(null)
    val bundleFlow: StateFlow<Bundle?> = _bundleFlow

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
            _bundleFlow.value = bundle
        }
    }
}