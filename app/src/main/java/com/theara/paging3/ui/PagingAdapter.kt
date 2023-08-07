package com.theara.paging3.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.theara.paging3.data.response.PagingRes
import com.theara.paging3.databinding.ItemPagingBinding
import com.theara.thpaging3lib.adapter.BasePagingAdapter
import javax.inject.Inject

class PagingAdapter @Inject constructor(): BasePagingAdapter<PagingRes, PagingAdapter.PageViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val binding =
            ItemPagingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PageViewHolder(binding)
    }

    override fun onBindData(holder: PageViewHolder, data: PagingRes?) {
        holder.bindTo(data)
    }

    inner class PageViewHolder(var binding: ItemPagingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindTo(data: PagingRes?) {
            binding.repoDescription.text = data?.description
            binding.repoName.text = data?.name
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PagingRes>() {
        override fun areItemsTheSame(oldItem: PagingRes, newItem: PagingRes): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: PagingRes, newItem: PagingRes): Boolean {
            return oldItem == newItem
        }
    }
}
