package com.theara.paging3library.adapter

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

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