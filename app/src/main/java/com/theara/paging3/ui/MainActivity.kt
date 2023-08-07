package com.theara.paging3.ui

import PagingStateHandler
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.theara.paging3.R
import com.theara.paging3.data.room.AppDatabase
import com.theara.paging3.databinding.ActivityMainBinding
import com.theara.thpaging3lib.adapter.PagingLoadingStateAdapter
import com.theara.thpaging3lib.utils.NetworkUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var pageAdapter: PagingAdapter

    @Inject
    lateinit var db: AppDatabase

    @Inject
    lateinit var networkUtils: NetworkUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        setUpAdapter()
        setUpState()
        observedData()
    }

    /**
     * method observed data by flow
     * */
    private fun observedData() {
        lifecycleScope.launch {
            viewModel.fetchData("Android", networkUtils.isNetworkAvailable()).collectLatest {
                pageAdapter.submitData(it)
            }
        }
    }

    /**
     * method setup adapter
     * */
    private fun setUpAdapter() {
        binding.rvDisplay.apply {
            adapter =
                pageAdapter.withLoadStateHeaderAndFooter(header = PagingLoadingStateAdapter { pageAdapter.retry() },
                    footer = PagingLoadingStateAdapter { pageAdapter.retry() })

        }
    }

    /**
     * method paging state handler
     * */
    private fun setUpState(){
        // Initialize the PagingStateHandler
        PagingStateHandler(
            pageAdapter = pageAdapter,
            lifecycleScope = lifecycleScope,
            isNetworkConnected = networkUtils.isNetworkAvailable(),
            onNoInternetAction = {
                binding.noInternet = true
                binding.isError = true
                binding.errorMessage =
                    resources.getString(com.theara.thpaging3lib.R.string.no_internet)
            },
            onErrorAction = { errorMessage ->
                binding.errorMessage = errorMessage
            },
            onNoDataAction = {
                binding.noData = true
            },
            onLoadingAction = {
                binding.isLoading = it
            }
        )
    }
}