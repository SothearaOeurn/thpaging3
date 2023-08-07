package com.theara.paging3library.adapter

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BasePagingAdapter<T : Any, VH : RecyclerView.ViewHolder>(diffCallback: DiffUtil.ItemCallback<T>) :
    PagingDataAdapter<T, VH>(diffCallback) {

    abstract fun onBindData(holder: VH, data: T?)

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindData(holder, getItem(position))
    }
}

