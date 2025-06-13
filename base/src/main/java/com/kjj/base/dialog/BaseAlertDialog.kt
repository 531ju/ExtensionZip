package com.kjj.base.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewbinding.ViewBinding
import com.kjj.base.R
import com.kjj.extension.getScreenWidthDP
import com.kjj.extension.value.dpToPx

abstract class BaseAlertDialog<B : ViewBinding> : DialogFragment() {

    private var _binding : B? = null
    val binding get() = _binding ?: throw NullPointerException("Binding instance must be not null.")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(layoutInflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            setBackgroundDrawableResource(android.R.color.transparent)

            attributes.windowAnimations = R.style.AlertDialogAnimation
            attributes = attributes.apply {
                width = if(context.getScreenWidthDP() >= 360) {
                    328.dpToPx()
                } else {
                    288.dpToPx()
                }.toInt()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    abstract fun inflateBinding(inflater : LayoutInflater, container : ViewGroup?, attachToParent : Boolean) : B
    abstract fun setupView()

    fun hide() {
        dialog?.hide()
    }

    fun show() {
        dialog?.show()
    }

    fun isShowing() = dialog?.isShowing == true
}