package com.theara.paging3.data.repo.paging

import androidx.paging.PagingData
import com.theara.paging3.data.response.PagingRes
import kotlinx.coroutines.flow.Flow

interface PagingRepo {
    fun getDataPaging(search: String?,isNetworkConnected:Boolean): Flow<PagingData<PagingRes>>
}