package com.kjj.extensionzip.base.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import com.kjj.extensionzip.databinding.ItemHolderEmptyBinding

@Suppress("UNCHECKED_CAST")
open class BasePagingAdapter<M : BaseAdapterModel> : PagingDataAdapter<BaseAdapterModel, BaseViewHolder<M>>(BaseAdapterModel.DIFF_CALLBACK) {
    override fun onBindViewHolder(holder: BaseViewHolder<M>, position: Int) {
        holder.bindData(getItem(position) as M)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<M> {
        val inflater = LayoutInflater.from(parent.context)
        return EmptyViewHolder(ItemHolderEmptyBinding.inflate(inflater, parent, false)) as BaseViewHolder<M>
    }

    fun setLoadStateListener(
        listener : (isLoading : Boolean, error : Exception?) -> Unit
    ) {
        addLoadStateListener {
            when(val state = it.refresh) {
                is LoadState.Error -> {
                    listener(
                        false,
                        if(state.error is Exception) state.error as Exception else null
                    )
                }
                is LoadState.Loading -> {
                    listener(true, null)
                }
                is LoadState.NotLoading -> {
                    listener(false, null)
                }
            }
        }
    }
}