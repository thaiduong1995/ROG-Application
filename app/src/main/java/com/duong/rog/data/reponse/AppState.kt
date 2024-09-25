package com.duong.rog.data.reponse

import androidx.annotation.Keep
import com.duong.rog.data.model.AppData

@Keep
sealed class AppState {
    data object IDLE : AppState()
    data object Loading : AppState()
    data class Success(val listApp: List<AppData>) : AppState()
    data class Error(val message: String) : AppState()
}