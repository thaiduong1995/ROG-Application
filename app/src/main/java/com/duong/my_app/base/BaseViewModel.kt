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

    abstract val TAG: String

    private val _navigationFlow: MutableStateFlow<NavigationCommand?> = MutableStateFlow(null)
    val navigationFlow: StateFlow<NavigationCommand?> = _navigationFlow

    fun navigate(navDirections: NavDirections) {
        _navigationFlow.value = NavigationCommand.ToDirection(navDirections)
    }

    fun navigateBack() {
        viewModelScope.launch(Dispatchers.IO) {
            _navigationFlow.value = NavigationCommand.ToBack
        }
    }

    open fun clearData() {
        _navigationFlow.value = null
    }
}