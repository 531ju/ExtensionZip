package com.kjj.extensionzip.base.dialogFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kjj.extensionzip.ext.view.showSnackBar
import java.lang.NullPointerException

abstract class BaseBottomSheetDialog<B : ViewBinding>() : TopRoundBottomSheetDialog() {

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

    abstract fun inflateBinding(inflater : LayoutInflater, container : ViewGroup?, attachToParent : Boolean) : B
    abstract fun setupView()
    abstract fun bindData()

    fun showMessage(msg : String) {
        (dialog?.window?.decorView ?: requireView()).showSnackBar(msg)
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
}