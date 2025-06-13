package com.kjj.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.kjj.extension.view.showSnackBar

abstract class BaseFragment<B : ViewBinding>() : Fragment() {

    private var _binding : B? = null
    val binding : B get() = _binding ?: throw NullPointerException("Binding instance must be not null.")

    private var backPressedCallback : OnBackPressedCallback? = null
    private var useDoubleBackToFinish = false
    private var backKeyPressedTime : Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    override fun onDestroyView() {
        super.onDestroyView()

        backPressedCallback?.remove()
        backPressedCallback = null
        _binding = null
    }

    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean) : B
    abstract fun setupView()
    abstract fun setupObserver()

    fun showMessage(msg : String) {
        requireView().showSnackBar(msg)
    }

    open fun setBackPressEnabled(
        isEnabled : Boolean,
        useDoubleBackToFinish : Boolean = false
    ) {
        if(isEnabled) {
            this.useDoubleBackToFinish = useDoubleBackToFinish

            backPressedCallback = object : OnBackPressedCallback(isEnabled) {
                override fun handleOnBackPressed() {
                    if(useDoubleBackToFinish) {
                        doubleBackToFinish()
                    }
                    onBackPressed()
                }
            }

            requireActivity().onBackPressedDispatcher.addCallback(
                viewLifecycleOwner,
                backPressedCallback as OnBackPressedCallback
            )
        }

        backPressedCallback?.isEnabled = isEnabled
    }

    protected open fun onBackPressed() { }

    protected fun doubleBackToFinish(
        message: String = "한번더 클릭하면 앱이 종료 됩니다."
    ) {
        if(System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            requireActivity().finish()
        } else {
            if(isAdded && isVisible) {
                backKeyPressedTime = System.currentTimeMillis()
                showMessage(message)
            }
        }
    }
}