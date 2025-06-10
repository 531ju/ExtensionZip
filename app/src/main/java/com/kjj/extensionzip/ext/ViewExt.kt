package com.kjj.extensionzip.ext

import android.view.View
import android.view.ViewGroup
import android.widget.EditText

fun ViewGroup.findEditChild(): EditText? {
    val childCount = childCount
    for (i in 0 until childCount) {
        val childView = getChildAt(i)
        if (childView is ViewGroup) this.findEditChild()
        else if (childView is EditText) return childView
    }
    return null
}

fun View.onSingleClick(action: () -> Unit) {
    var lastClickTime = 0L
    val delayMillis = 400L

    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > delayMillis) {
            lastClickTime = currentTime
            action()
        }
    }
}
