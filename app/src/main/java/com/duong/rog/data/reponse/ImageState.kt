package com.duong.rog.data.reponse

import com.duong.rog.data.model.Image

sealed class ImageState {
    data object Idle : ImageState()
    data object Prepare : ImageState()
    data object Loading : ImageState()
    data class Success(val imageList: List<Image>) : ImageState()
    data class Error(val message: String) : ImageState()
}