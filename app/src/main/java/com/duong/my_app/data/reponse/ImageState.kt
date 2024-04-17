package com.duong.my_app.data.reponse

import com.duong.my_app.data.model.Image

sealed class ImageState {
    data object IDLE : ImageState()
    data object Loading : ImageState()
    data class Success(val listImage: List<Image>) : ImageState()
    data class Error(val message: String) : ImageState()
}