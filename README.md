# Android Paging3 Library (thpaging3lib)

This repository demonstrates how to implement the Paging3 library in an Android app to efficiently load and display large datasets.

## Introduction

Paging3 is an Android library that helps manage and load large datasets in a way that's both memory-efficient and provides a smooth user experience during scrolling. It seamlessly integrates with RecyclerView and simplifies the process of loading data from various sources, such as network APIs or local databases.

This repository showcases the implementation of Paging3 in an Android app, along with explanations and code examples to help developers understand how to use the library effectively.

## Features

- Efficient loading of data in chunks, reducing memory usage and improving performance.
- Integration with RecyclerView for seamless scrolling and view recycling.
- Built-in support for handling network and database data sources.
- Automatic fetching of data as the user scrolls, providing a smooth browsing experience.
- Support for placeholders and error states in the UI during data loading.
- Customizable loading states and UI components to match your app's design.

## Getting Started

### Prerequisites

To run this project, you'll need the following tools:

- Android Studio 4.2+
- Kotlin 1.5+
- Android SDK

### Installation

1. Clone the repository:

```bash
git clone https://github.com/SothearaOeurn/thpaging3.git
```

## Usage

This section explains how to integrate and use the Paging3 library in your Android project step by step.

### 1. Add Paging3 Dependency

In your app's `build.gradle` file, add the Paging3 dependency:

Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
	    maven { url 'https://www.jitpack.io' }
	}
}
```
Add the dependency

```gradle
dependencies {
    implementation 'com.github.SothearaOeurn:thpaging3:1.0.6'
    // Add other dependencies as needed
}
```
### 2. Add code on View

In your app's `MainActivity` file, setup adapter and handle state with paging3:

```
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

```
### 3. Add code on RepoImpl

In your app's `PagingRepoImpl` file, Use with `createPagingSource` and `MyPagingState`:

```
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
                        MyPagingState.Error(Exception(context.getString(com.theara.thpaging3lib.R.string.empty_response_network)))
                    }
                } else {
                    return MyPagingState.Error(Exception(context.getString(com.theara.thpaging3lib.R.string.data_response_failed)))
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
                MyPagingState.Error(Exception(context.getString(com.theara.thpaging3lib.R.string.no_data_available)))
            }
        } catch (e: Exception) {
            return MyPagingState.Error(e)
        }
    }
}

