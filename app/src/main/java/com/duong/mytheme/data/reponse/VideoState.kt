package com.duong.mytheme.data.reponse

import com.duong.mytheme.data.model.Video

sealed class VideoState {
    data object IDLE : VideoState()
    data object Loading : VideoState()
    data class Success(val listVideo: List<Video>) : VideoState()
    data class Error(val message: String) : VideoState()
}