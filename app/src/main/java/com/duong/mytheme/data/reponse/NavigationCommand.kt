package com.duong.mytheme.data.reponse

import androidx.navigation.NavDirections

sealed class NavigationCommand {
    data class ToDirection(val directions: NavDirections) : NavigationCommand()
    data object ToBack : NavigationCommand()
}