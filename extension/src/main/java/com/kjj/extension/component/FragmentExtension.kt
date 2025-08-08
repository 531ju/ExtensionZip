package com.kjj.extension.component

import android.content.res.Configuration
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment

/**
 * Sets the status bar color of the hosting activity using the specified color resource.
 *
 * @param color The resource ID of the color to set as the status bar color.
 */
fun Fragment.setStatusBarColorRes(@ColorRes color: Int) {
    requireActivity().setStatusBarColor(requireContext().getColor(color))
}

fun Fragment.setNavigationBarColorRes(@ColorRes color: Int) {
    requireActivity().setNavigationBarColor(requireContext().getColor(color))
}

/**
 * Determines whether the current UI mode is set to dark (night) mode.
 *
 * @return `true` if the UI is in night mode; `false` otherwise.
 */
fun Fragment.isDarkMode() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

/**
 * Retrieves a material design color attribute from the fragment's root view.
 *
 * @param colorRes The attribute resource ID of the color to retrieve.
 * @return The resolved color value for the specified attribute.
 */
fun Fragment.getMaterialColor(@AttrRes colorRes: Int) = requireView().getMaterialColor(colorRes)

/**
 * Sets up the fragment's root view to adjust its top and bottom padding based on system window insets.
 *
 * Optionally includes keyboard (IME) insets and allows overriding the top and bottom padding values.
 *
 * @param top The explicit top padding to apply, or `null` to use the system inset.
 * @param bottom The explicit bottom padding to apply, or `null` to use the system inset.
 * @param includeKeyboardInsets Whether to include keyboard (IME) insets when calculating padding.
 */
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