package com.duong.rog.data.model

import androidx.annotation.Keep

/**
 * Created by Hưng Nguyễn on 20/01/2024
 * Phone: 0335236374
 * Email: nguyenhunghung2806@gmail.com
 */
@Keep
data class AppData(
    var appName : String = "",
    var packageName : String = "",
    var size: String = ""
)