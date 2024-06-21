package com.duong.my_app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video(
    var path: String = "",
) : Parcelable
