package com.duong.rog.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.duong.rog.data.reponse.NavigationCommand
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _navigationState: MutableStateFlow<NavigationCommand> =
        MutableStateFlow(NavigationCommand.Idle)
    val navigationFlow: StateFlow<NavigationCommand> = _navigationState

    fun navigate(navDirections: NavDirections) {
        _navigationState.value = NavigationCommand.ToDirection(navDirections)
    }

    fun navigateBack() {
        viewModelScope.launch(Dispatchers.IO) {
            _navigationState.value = NavigationCommand.ToBack
        }
    }

    open fun clearNavigation() {
        viewModelScope.launch(Dispatchers.IO) {
            _navigationState.value = NavigationCommand.Idle
        }
    }
}