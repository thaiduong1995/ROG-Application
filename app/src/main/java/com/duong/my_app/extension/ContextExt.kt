package com.duong.my_app.extension

import android.content.Context
import android.util.TypedValue
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "DATA_STORE")

fun Context.dpToPx(value: Int): Int {
    val metrics = this.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), metrics).toInt()
}