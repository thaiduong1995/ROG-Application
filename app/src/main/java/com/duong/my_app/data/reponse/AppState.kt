package com.duong.my_app.data.reponse

import com.duong.my_app.data.model.AppData
import com.duong.my_app.data.model.Video

sealed class AppState {
    data object IDLE : AppState()
    data object Loading : AppState()
    data class Success(val listApp: List<AppData>) : AppState()
    data class Error(val message: String) : AppState()
}