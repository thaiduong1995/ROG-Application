package com.duong.my_app.utls.rcv

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    // Does not work yet, https://issuetracker.google.com/issues/240585930
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelableArray(key: String): Array<T>? = when {
    // Does not work yet, https://issuetracker.google.com/issues/240585930
    SDK_INT >= 33 -> getParcelableArray(key, T::class.java)
    else -> {
        try {
            @Suppress("DEPRECATION") getParcelableArray(key) as? Array<T>?
        } catch (ex: ClassCastException) {
            null
        }
    }
}

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    // Does not work yet, https://issuetracker.google.com/issues/240585930
    //SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}
