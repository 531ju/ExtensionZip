package com.kjj.base.recyclerView

import com.kjj.base.databinding.ItemHolderEmptyBinding

class EmptyViewHolder(
    private val binding : ItemHolderEmptyBinding
) : BaseViewHolder<BaseAdapterModel>(binding) {

    override fun bindData(model: BaseAdapterModel) {
        // do nothing
    }
}