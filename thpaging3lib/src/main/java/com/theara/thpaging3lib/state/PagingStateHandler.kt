import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PagingStateHandler<T : Any>(
    private val pageAdapter: PagingDataAdapter<T, *>,
    private val lifecycleScope: CoroutineScope,
    private val isNetworkConnected: Boolean,
    private val onErrorAction: ((String) -> Unit)? = null,
    private val onNoDataAction: (() -> Unit)? = null,
    private val onNoInternetAction: (() -> Unit)? = null,
    private val onLoadingAction: ((Boolean) -> Unit)? = null
) {
    init {
        observeLoadState()
    }

    private fun observeLoadState() {
        lifecycleScope.launch {
            pageAdapter.loadStateFlow.collectLatest { loadStates ->
                val refreshState = loadStates.refresh
                val appendState = loadStates.append

                val isError = refreshState is LoadState.Error || appendState is LoadState.Error
                val isLoading = refreshState is LoadState.Loading
                onLoadingAction?.invoke(isLoading)

                if (isError) {
                    val error = when {
                        refreshState is LoadState.Error -> refreshState
                        appendState is LoadState.Error -> appendState
                        else -> null
                    }
                    error?.let {
                        val errorMessage = it.error.localizedMessage
                        onErrorAction?.invoke(errorMessage ?: "")
                    }
                    // Check for internet connectivity
                    if (!isNetworkConnected) {
                        onNoInternetAction?.invoke()
                    }
                }

                val noData =
                    refreshState is LoadState.NotLoading && appendState is LoadState.NotLoading && pageAdapter.itemCount == 0

                if (noData) {
                    onNoDataAction?.invoke()
                }
            }
        }
    }
}