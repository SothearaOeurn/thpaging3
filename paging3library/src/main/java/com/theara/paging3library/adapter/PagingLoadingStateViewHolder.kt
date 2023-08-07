package com.theara.paging3library.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.theara.paging3library.R
import com.theara.paging3library.databinding.ItemLoadStateBinding

class PagingLoadingStateViewHolder(
    private val binding: ItemLoadStateBinding,
    retry: () -> Unit,
    var context: Context
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.tbnRetry.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        binding.apply {
            val loading = loadState is LoadState.Loading
            val error = loadState is LoadState.Error
            isLoading = loading
            isErrorMessage = error
            if (error) {
                isRetry = true
                msgError = context.resources.getString(R.string.failed_load_data)
            } else {
                isRetry = false
                val isEmpty = loadState is LoadState.NotLoading && loadState.endOfPaginationReached
                if (isEmpty) {
                    isErrorMessage = true
                    msgError = context.resources.getString(R.string.more_data_available)
                } else {
                    isErrorMessage = false
                }
            }
        }
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): PagingLoadingStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_load_state, parent, false)
            val binding = ItemLoadStateBinding.bind(view)
            return PagingLoadingStateViewHolder(binding, retry, parent.context)
        }
    }
}