import androidx.paging.PagingSource
import androidx.paging.PagingState

sealed class MyPagingState<T> {
    data class Success<T>(val data: List<T>, val prevKey: Int?, val nextKey: Int?) :
        MyPagingState<T>()
    object Loading : MyPagingState<Nothing>()
    data class Error<T>(val error: Throwable) : MyPagingState<T>()
}

typealias PagingDataLoader<T> = suspend (Int) -> MyPagingState<T>

fun <T : Any> createPagingSource(pagingDataLoader: PagingDataLoader<T>): PagingSource<Int, T> {
    return object : PagingSource<Int, T>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
            return try {
                val nextPage = params.key ?: 1 // Set default page number to 1 if the key is null
                when (val result = pagingDataLoader(nextPage)) {
                    is MyPagingState.Success -> {
                        LoadResult.Page(
                            data = result.data,
                            prevKey = result.prevKey,
                            nextKey = result.nextKey
                        )
                    }
                    is MyPagingState.Error -> {
                        LoadResult.Error(result.error)
                    }
                    else -> {
                        LoadResult.Error(Exception("Invalid response type"))
                    }
                }
            } catch (e: Exception) {
                LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, T>): Int? {
            return state.anchorPosition?.let { anchorPosition ->
                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
            }
        }
    }
}

