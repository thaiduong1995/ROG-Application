package com.duong.mytheme.extension

import android.content.Context
import android.util.TypedValue

fun Context.dpToPx(value: Int): Int {
    val metrics = this.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), metrics).toInt()
}