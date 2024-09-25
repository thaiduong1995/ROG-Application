package com.duong.rog.data.reponse

import androidx.annotation.Keep
import androidx.navigation.NavDirections

@Keep
sealed class NavigationCommand {
    data object Idle : NavigationCommand()
    data class ToDirection(val directions: NavDirections) : NavigationCommand()
    data object ToBack : NavigationCommand()
}