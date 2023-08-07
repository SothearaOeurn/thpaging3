package com.theara.paging3.data.response

import com.google.gson.annotations.SerializedName

data class BasePagingRes(
    @SerializedName("total_count") val total: Int = 0,
    @SerializedName("items") val items: List<PagingRes> = emptyList(),
    val nextPage: Int? = null
)