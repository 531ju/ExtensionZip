package com.kjj.extensionzip.ext.value

import android.content.res.Resources

fun Float.dpToPx() : Float {
    return (this * Resources.getSystem().displayMetrics.density)
}