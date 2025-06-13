package com.kjj.base.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kjj.extension.view.showSnackBar

abstract class BaseBottomSheetDialog<B : ViewBinding> : TopRoundBottomSheetDialog() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
    }

    override fun onStart() {
        super.onStart()
        val behavior = BottomSheetBehavior.from(requireView().parent as View)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun inflateBinding(inflater : LayoutInflater, container : ViewGroup?, attachToParent : Boolean) : B
    abstract fun setupView()

    fun showMessage(msg : String) {
        (dialog?.window?.decorView ?: requireView()).showSnackBar(msg)
    }
}