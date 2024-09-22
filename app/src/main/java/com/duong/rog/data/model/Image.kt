package com.duong.rog.data.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class Image(
    var name: String = "",
    var path: String = "",
    var isSelected: Boolean = false
) : Parcelable
