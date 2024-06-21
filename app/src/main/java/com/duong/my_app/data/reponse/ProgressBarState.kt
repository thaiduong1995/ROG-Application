package com.duong.my_app.data.reponse

import androidx.annotation.Keep

@Keep
sealed class ProgressBarState {
    data object Idle : ProgressBarState()
    data object LoadingData : ProgressBarState()
    data object HandlerData : ProgressBarState()
}