package com.kjj.extensionzip.base.recyclerView

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<M : BaseAdapterModel> : RecyclerView.ViewHolder {
    constructor(binding : ViewBinding) : super(binding.root)
    constructor(view : View) : super(view)

    open fun animate() {}
    open fun reset() {}
    abstract fun bindData(model : M)
}