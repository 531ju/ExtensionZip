package com.kjj.extensionzip.ext

import android.app.Activity
import android.content.res.Configuration
import android.os.Build
import android.view.WindowInsets
import androidx.annotation.ColorInt

fun Activity.setStatusBarColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        // Android 15+
        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
            view.setBackgroundColor(color)

            // Adjust padding to avoid overlap
            view.setPadding(0, statusBarInsets.top, 0, 0)
            insets
        }
    } else {
        // For Android 14 and below
        window.statusBarColor = color
    }
}

fun Activity.setNavigationBarColor(@ColorInt color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
        // Android 15+
        window.decorView.setOnApplyWindowInsetsListener { view, insets ->
            val navigationBarInsets = insets.getInsets(WindowInsets.Type.navigationBars())
            view.setBackgroundColor(color)

            // Adjust padding to avoid overlap
            view.setPadding(0, 0, 0, navigationBarInsets.bottom)
            insets
        }
    } else {
        // For Android 14 and below
        window.navigationBarColor = color
    }
}

fun Activity.isDarkMode() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES