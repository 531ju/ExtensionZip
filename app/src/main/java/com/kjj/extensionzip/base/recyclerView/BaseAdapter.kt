package com.kjj.extensionzip.base.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.kjj.extensionzip.databinding.ItemHolderEmptyBinding

@Suppress("UNCHECKED_CAST")
class BaseAdapter<M : BaseAdapterModel>(
    private var modelList : List<BaseAdapterModel>,
    private var adapterListener: AdapterListener? = null
) : ListAdapter<BaseAdapterModel, BaseViewHolder<M>>(BaseAdapterModel.DIFF_CALLBACK) {
    fun getAllItems(): List<BaseAdapterModel> {
        return modelList
    }

    fun getOneItem(pos: Int):  BaseAdapterModel {
        return modelList[pos]
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<M> {
        val inflater = LayoutInflater.from(parent.context)
        return EmptyViewHolder(
            ItemHolderEmptyBinding.inflate(
                inflater,
                parent,
                false
            )
        ) as BaseViewHolder<M>
    }

    override fun onBindViewHolder(holder: BaseViewHolder<M>, position: Int) {
        val holderPosition = holder.adapterPosition

        if(modelList.lastIndex < holderPosition) {
            return
        }

        try {
            holder.bindData(modelList[holderPosition] as M)
        }catch (e: ClassCastException) {
            e.printStackTrace()
        }
    }

    override fun submitList(list: MutableList<BaseAdapterModel>?) {
        list?.let {
            modelList = it
            super.submitList(list)
        }
    }

    override fun onViewRecycled(holder: BaseViewHolder<M>) {
        super.onViewRecycled(holder)
    }
}

inline fun <reified T> List<BaseAdapterModel>.checkType() = all { it is T }

interface AdapterListener