package com.duong.rog.data.reponse

import com.duong.rog.data.model.AppData

sealed class AppState {
    data object IDLE : AppState()
    data object Loading : AppState()
    data class Success(val listApp: List<AppData>) : AppState()
    data class Error(val message: String) : AppState()
}