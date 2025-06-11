package com.kjj.extensionzip.ext.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup.LayoutParams
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible

fun View.fadeOut(animationEndListener : (() -> Unit)? = null) {
    val shortAnimationDuration =
        context.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
    alpha = 1f

    animate()
        .alpha(0f)
        .setDuration(shortAnimationDuration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if(alpha == 0f) {
                    isVisible = false
                }
                animationEndListener?.invoke()
            }
        })
}

fun View.fadeIn(
    animationEndListener: (() -> Unit)? = null
) {
    val animDuration = context.resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()

    alpha = 0f
    isVisible = true

    animate()
        .alpha(1f)
        .setDuration(animDuration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if(alpha == 1f) {
                    isVisible = true
                }
                animationEndListener?.invoke()
            }
        })
}

fun View.expand(
    duration: Long = 100
) {
    val matchParentMeasureSpec = MeasureSpec.makeMeasureSpec((parent as View).width, MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
    measure(matchParentMeasureSpec, wrapContentMeasureSpec)

    val targetHeight = measuredHeight

    layoutParams.height = 0
    isVisible = true

    ValueAnimator.ofFloat(0f, 1f).apply {
        addUpdateListener {
            val value = it.animatedValue as Float

            layoutParams.height = if(value == 1f) {
                LayoutParams.WRAP_CONTENT
            }else {
                (targetHeight * value).toInt()
            }

            requestLayout()
        }

        this.duration = duration
    }?.run {
        start()
    }
}

fun View.collapse(
    duration: Long = 100
) {
    val initHeight = measuredHeight

    ValueAnimator.ofFloat(0f, 1f).apply {
        addUpdateListener {
            val value = it.animatedValue as Float

            if(value == 1f) {
                isVisible = false
            }else {
                layoutParams.height = initHeight - (initHeight * value).toInt()
            }

            requestLayout()
        }

        this.duration = duration
    }?.run {
        start()
    }
}

fun View.getCurrentBackgroundColor(): Int? {
    return when(val backgroundDrawable = background) {
        is ColorDrawable -> backgroundDrawable.color
        else            -> null
    }
}

fun View.changeColorAnimation(animDuration: Long, endColor: Int) {
    getCurrentBackgroundColor()?.takeIf { color->
        color!=endColor
    }?.let { current->
        ValueAnimator.ofObject(ArgbEvaluator(), current, ContextCompat.getColor(context, endColor)).apply {
            addUpdateListener { animator->
                setBackgroundColor(animator.animatedValue as Int)
            }
            duration = animDuration
        }?.run {
            start()
        }
    }?: setBackgroundColor(ContextCompat.getColor(context, endColor))
}

