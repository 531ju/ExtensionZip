package com.kjj.base.recyclerView

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import java.io.Serializable

abstract class BaseAdapterModel(
    open val id : Long?
) : Serializable {
    companion object {
        val DIFF_CALLBACK : DiffUtil.ItemCallback<BaseAdapterModel> = object : DiffUtil.ItemCallback<BaseAdapterModel>() {
            override fun areItemsTheSame(oldItem: BaseAdapterModel, newItem: BaseAdapterModel): Boolean {
                return oldItem.id == newItem.id
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: BaseAdapterModel, newItem: BaseAdapterModel): Boolean {
                return oldItem === newItem
            }
        }
    }
}