package com.kjj.extensionzip.base.dialogFragment

import android.app.Dialog
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kjj.extensionzip.R

open class TopRoundBottomSheetDialog : BottomSheetDialogFragment() {

    override fun getTheme(): Int {
        return R.style.TopRoundBottomSheetDialogTheme
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }
}