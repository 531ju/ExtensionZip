package com.kjj.extension.component

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.snackbar.Snackbar
import kotlin.math.abs

fun Snackbar.swipeOut(delaX : Float) {
    val parentView = view
    val direction = if(delaX > 0f) 1f else -1f
    ViewCompat.animate(parentView)
        .translationX(direction * parentView.width)
        .setInterpolator(FastOutSlowInInterpolator())
        .setDuration(250L)
        .start()
    ViewCompat.animate(parentView)
        .alpha(0f)
        .setDuration(250L)
        .setListener(object : ViewPropertyAnimatorListener {
            override fun onAnimationStart(view: View) {
            }

            override fun onAnimationEnd(view: View) {
                view.setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
                dismiss()
            }

            override fun onAnimationCancel(view: View) {
            }

        })
        .start()
}

fun Snackbar.swipeIn(deltaX : Float) {
    val parentView = view
    if (abs(deltaX) >= 0f) {
        ViewCompat.animate(parentView)
            .translationX(0f)
            .setInterpolator(FastOutSlowInInterpolator())
            .setDuration(250L)
            .setListener(object : ViewPropertyAnimatorListener {
                override fun onAnimationStart(view: View) {
                }

                override fun onAnimationEnd(view: View) {
                }

                override fun onAnimationCancel(view: View) {
                }

            })      // Remove listener that is attached in animateViewIn() of BaseTransientBottomBar
            .start()
        ViewCompat.animate(parentView)
            .alpha(1f)
            .setDuration(250L)
            .start()
    } else {
        // Else, just make sure the layout is at correct position
        parentView.translationX = 0f
        parentView.alpha = 1f
    }
}