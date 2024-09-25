package com.duong.rog.data.reponse

import androidx.annotation.Keep
import com.duong.rog.data.model.Video

@Keep
sealed class VideoState {
    data object IDLE : VideoState()
    data object Loading : VideoState()
    data class Success(val videoList: List<Video>) : VideoState()
    data class Error(val message: String) : VideoState()
}