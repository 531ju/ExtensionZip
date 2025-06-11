package com.kjj.extensionzip.ext.value

import android.content.res.Resources
import androidx.core.graphics.ColorUtils
import kotlin.math.round

fun Int.dpToPx() : Float {
    return (this * Resources.getSystem().displayMetrics.density)
}

fun Int.splitHourMin() : Pair<Int, Int> {
    val hour = this / 60
    val min = this % 60
    return Pair(hour, min)
}

fun Int.setAlpha(alpha: Float): Int {
    return ColorUtils.setAlphaComponent(
        this,
        round((round(alpha * 100) / 100.0) * 255).toInt()
    )
}
