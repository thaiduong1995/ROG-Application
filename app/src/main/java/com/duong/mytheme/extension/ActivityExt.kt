package com.duong.mytheme.extension

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Activity.showSnackBar(view: View?, message: String) {
    Handler(Looper.getMainLooper()).postDelayed({
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }, 500)
}