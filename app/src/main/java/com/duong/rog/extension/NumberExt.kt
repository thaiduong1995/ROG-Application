package com.duong.rog.extension

import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

fun Long.readableFileSize(): String {
    if (this <= 0) return "0"
    val units = arrayOf("B", "kB", "MB", "GB", "TB", "PB", "EB")
    val digitGroups = (log10(this.toDouble()) / log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(this / 1024.0.pow(digitGroups)) + " " + units[digitGroups]
}