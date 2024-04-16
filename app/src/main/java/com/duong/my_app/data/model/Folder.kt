package com.duong.my_app.data.model

import androidx.annotation.Keep

/**
 * Created by Hưng Nguyễn on 20/01/2024
 * Phone: 0335236374
 * Email: nguyenhunghung2806@gmail.com
 */
@Keep
data class Folder(
    var name : String = "",
    var path : String = "",
    var size: Int = 0
)