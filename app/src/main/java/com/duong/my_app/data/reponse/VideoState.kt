package com.duong.my_app.data.reponse

import com.duong.my_app.data.model.Video

sealed class VideoState {
    data object IDLE : VideoState()
    data object Loading : VideoState()
    data class Success(val listVideo: List<Video>) : VideoState()
    data class Error(val message: String) : VideoState()
}