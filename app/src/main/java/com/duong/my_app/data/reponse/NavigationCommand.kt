package com.duong.my_app.data.reponse

import androidx.navigation.NavDirections

sealed class NavigationCommand {
    data class ToDirection(val directions: NavDirections) : NavigationCommand()
    data object ToBack : NavigationCommand()
}