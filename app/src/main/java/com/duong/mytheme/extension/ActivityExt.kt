package com.duong.mytheme.extension

import android.app.Activity
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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

fun Activity.hideStatusBar() {
    window?.apply {
        when {
            Build.VERSION.SDK_INT in 21..29 -> {
                setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                )
            }

            Build.VERSION.SDK_INT >= 30 -> {
                val windowInsetsController =
                    WindowCompat.getInsetsController(this, this.decorView)
                // Configure the behavior of the hidden system bars.
                windowInsetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

                // Add a listener to update the behavior of the toggle fullscreen button when
                // the system bars are hidden or revealed.
                decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
                    // You can hide the caption bar even when the other system bars are visible.
                    // To account for this, explicitly check the visibility of navigationBars()
                    // and statusBars() rather than checking the visibility of systemBars().
                    if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
                        || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())
                    ) {
                        // Hide both the status bar and the navigation bar.
                        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
                    }
                    view.onApplyWindowInsets(windowInsets)
                }
            }
        }
    }
}

fun Activity.showStatusBar() {
    window?.apply {
        when {
            Build.VERSION.SDK_INT in 21..29 -> {
                clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            }

            Build.VERSION.SDK_INT >= 30 -> {
                val windowInsetsController =
                    WindowCompat.getInsetsController(this, this.decorView)
                // Configure the behavior of the hidden system bars.
                windowInsetsController.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

                // Add a listener to update the behavior of the toggle fullscreen button when
                // the system bars are hidden or revealed.
                decorView.setOnApplyWindowInsetsListener { view, windowInsets ->
                    // You can hide the caption bar even when the other system bars are visible.
                    // To account for this, explicitly check the visibility of navigationBars()
                    // and statusBars() rather than checking the visibility of systemBars().
                    if (!(windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
                                || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars()))
                    ) {
                        // Show both the status bar and the navigation bar.
                        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
                    }
                    view.onApplyWindowInsets(windowInsets)
                }
            }
        }
    }
}