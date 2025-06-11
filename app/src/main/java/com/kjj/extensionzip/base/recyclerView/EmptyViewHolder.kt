package com.kjj.extensionzip.base.recyclerView

import com.kjj.extensionzip.databinding.ItemHolderEmptyBinding

class EmptyViewHolder(
    private val binding : ItemHolderEmptyBinding
) : BaseViewHolder<BaseAdapterModel>(binding) {

    override fun bindData(model: BaseAdapterModel) {
        // to nothing
    }
}