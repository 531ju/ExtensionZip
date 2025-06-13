package com.kjj.extension.value

import android.content.res.Resources

fun Float.dpToPx() : Float {
    return (this * Resources.getSystem().displayMetrics.density)
}