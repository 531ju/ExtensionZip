package com.kjj.extension.component

import android.content.res.Configuration
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment

fun Fragment.setStatusBarColorRes(@ColorRes color: Int) {
    requireActivity().setStatusBarColor(requireContext().getColor(color))
}

fun Fragment.setNavigationBarColorRes(@ColorRes color: Int) {
    requireActivity().setNavigationBarColor(requireContext().getColor(color))
}

fun Fragment.isDarkMode() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

fun Fragment.getMaterialColor(@AttrRes colorRes: Int) = requireView().getMaterialColor(colorRes)

fun Fragment.setupDefaultWindowInsets(
    top: Int? = null,
    bottom: Int? = null,
    includeKeyboardInsets: Boolean = true
) {
    ViewCompat.setOnApplyWindowInsetsListener(requireView()) { v, insets ->
        val systemBarsInsets = if(includeKeyboardInsets) {
            insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.displayCutout() or
                        WindowInsetsCompat.Type.ime()
            )
        } else {
            insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                        WindowInsetsCompat.Type.displayCutout()
            )
        }

        v.updatePadding(
            top = top ?: systemBarsInsets.top,
            bottom = bottom ?: systemBarsInsets.bottom
        )

        insets
    }
}