package com.kjj.extension.component

import android.content.res.Configuration
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment

fun Fragment.setStatusBarColorRes(@ColorRes color: Int) {
    requireActivity().setStatusBarColor(requireContext().getColor(color))
}

fun Fragment.setNavigationBarColorRes(@ColorRes color: Int) {
    requireActivity().setNavigationBarColor(requireContext().getColor(color))
}

fun Fragment.isDarkMode() = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

fun Fragment.getMaterialColor(@AttrRes colorRes: Int) = requireView().getMaterialColor(colorRes)