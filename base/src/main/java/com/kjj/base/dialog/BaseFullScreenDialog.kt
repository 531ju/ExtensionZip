package com.kjj.base.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.kjj.base.R

abstract class BaseFullScreenDialog<B : ViewBinding> : DialogFragment {
    companion object {
        const val ANIMATION_TYPE_SLIDE_UP_DOWN = 1
        const val ANIMATION_TYPE_SLIDE_IN_OUT = 2
    }

    private var _binding : B? = null
    val binding get() = _binding ?: throw NullPointerException("Binding instance must be not null")

    private var animationType = ANIMATION_TYPE_SLIDE_UP_DOWN
    private var isPause = false

    constructor()
    constructor(animationType : Int) {
        this.animationType = animationType
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Theme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupObserver()
    }

    override fun onResume() {
        super.onResume()
        setAnimation()
    }

    override fun onPause() {
        super.onPause()

        isPause = true
        setAnimation()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun inflateBinding(inflater : LayoutInflater, container : ViewGroup?, attachToParent : Boolean) : B
    abstract fun setupView()
    abstract fun setupObserver()

    private fun setAnimation(animationType: Int? = null) {
        dialog?.window?.setWindowAnimations(
            animationType?.let {
                if(isPause) {
                    isPause = false

                    if(animationType == ANIMATION_TYPE_SLIDE_UP_DOWN) {
                        R.style.DialogAnimationSlideUpDown
                    } else {
                        R.style.DialogAnimationSlideOut
                    }
                } else {
                    if(animationType == ANIMATION_TYPE_SLIDE_UP_DOWN) {
                        R.style.DialogAnimationSlideUpDown
                    } else {
                        R.style.DialogAnimationSlideOut
                    }
                }
            } ?: 0
        )
    }
}