package com.theara.paging3.data.repo.paging

import MyPagingState
import android.content.Context
import androidx.paging.*
import com.theara.paging3.data.resclient.Api
import com.theara.paging3.data.response.PagingRes
import com.theara.paging3.data.room.AppDatabase
import com.theara.paging3.utils.Constants.NETWORK_PAGE_SIZE
import createPagingSource
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class PagingRepoImpl @Inject constructor(private var api: Api, private var context: Context) :
    PagingRepo {

    @Inject
    lateinit var dao: AppDatabase

    override fun getDataPaging(search: String?,isNetworkConnected:Boolean): Flow<PagingData<PagingRes>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                createPagingSource { page ->
                    myApiPagingLoader(
                        search,
                        page,
                        isNetworkConnected
                    )
                }
            }
        ).flow
    }

    /**
     * method api paging loader
     * */
    private suspend fun myApiPagingLoader(
        search: String?,
        page: Int,
        shouldFetchFromNetwork: Boolean
    ): MyPagingState<PagingRes> {
        try {
            val cachedData = dao.PagingDao().getProData()
            if (shouldFetchFromNetwork) {
                val response = api.searchRepos(search, page, NETWORK_PAGE_SIZE)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    return if (responseBody != null) {
                        dao.PagingDao().deleteAll()
                        responseBody.items.forEach {
                            dao.PagingDao().insertAll(it)
                        }
                        MyPagingState.Success(
                            responseBody.items,
                            if (page > 1) page - 1 else null,
                            if (responseBody.items.isNotEmpty()) page + 1 else null
                        )
                    } else {
                        MyPagingState.Error(Exception(context.getString(com.theara.paging3library.R.string.empty_response_network)))
                    }
                } else {
                    return MyPagingState.Error(Exception(context.getString(com.theara.paging3library.R.string.data_response_failed)))
                }
            }
            // If network call failed or there is cached data available, return the cached data
            return if (cachedData.isNotEmpty()) {
                MyPagingState.Success(
                    cachedData,
                    if (page > 1) page - 1 else null,
                    if (cachedData.isNotEmpty()) page + 1 else null
                )
            } else {
                // If there is no cached data and the network call also failed,
                // return an error to indicate that there is no data available.
                MyPagingState.Error(Exception(context.getString(com.theara.paging3library.R.string.no_data_available)))
            }
        } catch (e: Exception) {
            return MyPagingState.Error(e)
        }
    }
}


