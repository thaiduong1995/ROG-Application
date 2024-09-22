package com.duong.rog.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video(
    var path: String = "",
) : Parcelable
