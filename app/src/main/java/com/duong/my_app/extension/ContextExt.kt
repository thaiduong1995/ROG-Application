package com.duong.my_app.extension

import android.app.AppOpsManager
import android.content.Context
import android.os.Build
import android.util.TypedValue
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "DATA_STORE")

fun Context.dpToPx(value: Int): Int {
    val metrics = this.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), metrics).toInt()
}

fun Context.requestManagerApp(): Boolean {
    return try {
        val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        val appManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val modeApp: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appManager.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                applicationInfo.uid,
                applicationInfo.packageName
            )
        } else appManager.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            applicationInfo.uid,
            applicationInfo.packageName
        )
        modeApp == AppOpsManager.MODE_ALLOWED
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}