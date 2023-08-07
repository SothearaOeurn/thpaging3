package com.theara.paging3.data.resclient

import com.theara.paging3.data.response.BasePagingRes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

/*    @GET("search/repositories?sort=stars")
    suspend fun searchRepos(
        @Query("q") query: String?,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ):Response<BasePagingRes>*/

    @GET("search/repositories?sort=stars")
    suspend fun searchRepos(
        @Query("q") query: String?,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ):Response<BasePagingRes>

    @GET("search/repositories?sort=stars")
    suspend fun searchRepos2(
        @Query("q") query: String = "Android",
        @Query("page") page: Int = 1,
        @Query("per_page") itemsPerPage: Int = 20
    ): BasePagingRes
}