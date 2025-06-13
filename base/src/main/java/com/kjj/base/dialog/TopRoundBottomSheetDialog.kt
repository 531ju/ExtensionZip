package com.kjj.base.dialog

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kjj.base.R

open class TopRoundBottomSheetDialog : BottomSheetDialogFragment() {

    override fun getTheme(): Int {
        return R.style.TopRoundBottomSheetDialogTheme
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }
}