package com.kjj.extensionzip.ext.value

import android.content.res.Resources

fun Int.dpToPx() : Float {
    return (this * Resources.getSystem().displayMetrics.density)
}