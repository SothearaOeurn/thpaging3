package com.theara.thpaging3lib.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.theara.thpaging3lib.adapter.PagingLoadingStateViewHolder

class PagingLoadingStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<PagingLoadingStateViewHolder>() {
    override fun onBindViewHolder(holder: PagingLoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PagingLoadingStateViewHolder {
        return PagingLoadingStateViewHolder.create(parent, retry)
    }
}