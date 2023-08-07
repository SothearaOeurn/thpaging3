package com.theara.paging3.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.theara.paging3.data.repo.paging.PagingRepoImpl
import com.theara.paging3.data.response.PagingRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var repo: PagingRepoImpl

    fun fetchData(search: String, isNetworkConnected: Boolean): Flow<PagingData<PagingRes>> {
        return repo.getDataPaging(search, isNetworkConnected).cachedIn(viewModelScope)
    }
}