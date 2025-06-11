package com.kjj.extensionzip.ext.view

import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.AttrRes
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.kjj.extensionzip.ext.component.swipeIn
import com.kjj.extensionzip.ext.component.swipeOut
import kotlin.math.abs

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

fun View.getMaterialColor(
    @AttrRes colorRes: Int
) = MaterialColors.getColor(this, colorRes)

fun View.showSnackBar(msg : String, dismissToSwipe : Boolean = true) {
    val startAlphaSwipeDistance = 0.1f
    val endAlphaSwipeDistance = 0.6f
    val swipeOutVelocity = 800f

    val snackBar = Snackbar.make(this, msg, Snackbar.LENGTH_SHORT)

    val snackBarText = snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
    snackBarText?.maxLines = 10

    if(dismissToSwipe) {
        snackBar.view.setOnTouchListener(object : View.OnTouchListener {
            var startX: Float = 0f
            var endX: Float = 0f
            var velocityTracker: VelocityTracker? = null

            fun getTracker(): VelocityTracker {
                if (velocityTracker == null) {
                    velocityTracker = VelocityTracker.obtain()
                }
                return velocityTracker!!
            }

            fun recycleVelocityTracker() {
                velocityTracker?.recycle()
                velocityTracker = null
            }

            override fun onTouch(view: View?, event: MotionEvent?): Boolean {
                val index = event?.actionIndex ?: return true
                val pointerId = event.getPointerId(index)
                when(event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = event.rawX
                        getTracker().addMovement(event)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        getTracker().addMovement(event)
                        val moveX = event.rawX
                        val deltaX = moveX - startX
                        val snackBarView = snackBar.view
                        snackBarView.translationX = deltaX
                        val totalWidth = snackBarView.measuredWidth
                        val fractionTravelled = abs(deltaX / totalWidth)
                        when {
                            fractionTravelled < startAlphaSwipeDistance -> snackBarView.alpha = 1f
                            fractionTravelled > endAlphaSwipeDistance -> snackBarView.alpha = 0f
                            else -> snackBarView.alpha = 1f - (fractionTravelled - startAlphaSwipeDistance) / (endAlphaSwipeDistance - startAlphaSwipeDistance)
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        endX = event.rawX
                        val velocityTracker = getTracker()
                        velocityTracker.computeCurrentVelocity(1000)
                        val velocity = abs(velocityTracker.getXVelocity(pointerId))
                        recycleVelocityTracker()
                        var toSwipeOut = false
                        var snackBarView = snackBar.view
                        if(abs(endX - startX) / snackBarView.width > 0.5) {
                            toSwipeOut = true
                        }
                        if(abs(velocity) > swipeOutVelocity) {
                            toSwipeOut = true
                        }
                        if(toSwipeOut) {
                            snackBar.swipeOut(endX - startX)
                        } else {
                            snackBar.swipeIn(endX - startX)
                        }
                        snackBar.view.performClick()
                    }

                }
                return true
            }
        })
    }

    snackBar.show()
}
