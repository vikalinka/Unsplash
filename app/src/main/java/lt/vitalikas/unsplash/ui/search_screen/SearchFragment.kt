package lt.vitalikas.unsplash.ui.search_screen

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.work.WorkInfo
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatus
import lt.vitalikas.unsplash.data.services.DislikePhotoWorker
import lt.vitalikas.unsplash.data.services.LikePhotoWorker
import lt.vitalikas.unsplash.databinding.FragmentSearchBinding
import lt.vitalikas.unsplash.utils.autoCleaned
import lt.vitalikas.unsplash.utils.onTextChangedFlow
import lt.vitalikas.unsplash.utils.showInfo
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val searchList get() = binding.searchRecyclerView
    private val noConnectionText get() = binding.noConnectionTextView
    private val loadingProgress get() = binding.loadingProgressBar
    private val refreshLayout get() = binding.searchSwipeRefreshLayout
    private val toolbar get() = binding.searchToolbar
    private val search get() = binding.queryTextInput

    private val searchViewModel by viewModels<SearchViewModel>()

    private lateinit var id: String

    private val searchAdapter by autoCleaned {
        SearchAdapter(
            onItemClick = { id ->
                val directions =
                    SearchFragmentDirections.actionSearchFragmentToFeedDetailsFragment(id)
                findNavController().navigate(directions)
            },
            onLikeClick = { id ->
                this.id = id

                searchViewModel.likePhoto(id)
            },
            onDislikeClick = { id ->
                this.id = id

                searchViewModel.dislikePhoto(id)
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchList()
        initAdapterRefresh()
        observeNetworkConnection()
        observeSearchResults()
        setupToolbar()
        handleToolbarNavigation()
        observeLikingPhoto()
        observeDislikingPhoto()
    }

    private fun initSearchList() {
        with(searchList) {
            val searchLoadStateAdapter = SearchLoadStateAdapter {
                searchAdapter.retry()
            }

            val concatAdapter = searchAdapter.withLoadStateFooter(
                footer = searchLoadStateAdapter
            )

            adapter = concatAdapter

            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

            setHasFixedSize(true)

            addItemDecoration(SearchOffsetDecoration(requireContext()))
        }
    }

    private fun observeSearchResults() {
        val query = search.onTextChangedFlow()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    searchViewModel.getSearchData(query)
                        .collectLatest { data ->
                            searchAdapter.submitData(data)
                            refreshLayout.isRefreshing = false
                        }
                }
            }
        }
    }

    private fun observeNetworkConnection() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    searchViewModel.networkStatus.collect { status ->
                        when (status) {
                            NetworkStatus.Available -> {
                                noConnectionText.isVisible = false
                                // retry after connection re-established
                                searchAdapter.retry()
                            }
                            NetworkStatus.Unavailable -> {
                                noConnectionText.isVisible = true
                                showInfo("No internet connection. Cached data is shown.")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initAdapterRefresh() {
        refreshLayout.setOnRefreshListener {
            searchAdapter.refresh()
        }
    }

    private fun setupToolbar() {
        with(toolbar) {
            title = "SEARCH"

            inflateMenu(R.menu.feed_toolbar_menu)

//            setOnMenuItemClickListener { menuItem ->
//                when (menuItem.itemId) {
//                    R.id.toolbar_menu_search -> {
//
//                        menuItem.setOnActionExpandListener(object :
//                            MenuItem.OnActionExpandListener {
//                            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
//                                showToast("search expanded")
//                                return true
//                            }
//
//                            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
//                                showToast("search collapsed")
//                                return true
//                            }
//                        })
//
//                        with(menuItem.actionView as SearchView) {
//                            this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//                                override fun onQueryTextSubmit(p0: String?): Boolean {
//                                    return true
//                                }
//
//                                override fun onQueryTextChange(text: String?): Boolean {
//                                    return true
//                                }
//                            })
//                        }
//
//                        true
//                    }
//
//                    else -> false
//                }
//            }
        }
    }

    private fun handleToolbarNavigation() {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun observeLikingPhoto() {
        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData(LikePhotoWorker.LIKE_PHOTO_WORK_ID_FROM_FEED)
            .observe(viewLifecycleOwner) { workInfos ->
                if (workInfos.isNullOrEmpty()) {
                    return@observe
                }
                when (workInfos.first().state) {
                    WorkInfo.State.ENQUEUED -> {
                        Timber.d("LIKING PHOTO ENQUEUED")
                    }
                    WorkInfo.State.RUNNING -> {
                        Timber.d("LIKING PHOTO RUNNING")
                    }
                    WorkInfo.State.FAILED -> {
                        Timber.d("LIKING PHOTO FAILED")
                        WorkManager.getInstance(requireContext()).pruneWork()
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        Timber.d("LIKING PHOTO SUCCEEDED")
                        WorkManager.getInstance(requireContext()).pruneWork()
                        updateDataOnFeedLike()
                    }
                    WorkInfo.State.CANCELLED -> {
                        Timber.d("LIKING PHOTO CANCELED")
                        WorkManager.getInstance(requireContext()).pruneWork()
                    }
                    WorkInfo.State.BLOCKED -> {
                        Timber.d("LIKING PHOTO BLOCKED")
                    }
                }
            }
    }

    private fun updateDataOnFeedLike() {
        // getting data from paging adapter`s snapshot
        val snapshotItem = searchAdapter.snapshot().firstOrNull { snapshotItem ->
            snapshotItem?.id == this.id
        }

        snapshotItem?.let {
            // updating snapshot data
            it.likedByUser = true
            it.likes += 1

            // updating paging data adapter
            searchAdapter.notifyDataSetChanged()
        }
    }

    private fun observeDislikingPhoto() {
        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData(DislikePhotoWorker.DISLIKE_PHOTO_WORK_ID_FROM_FEED)
            .observe(viewLifecycleOwner) { workInfos ->
                if (workInfos.isNullOrEmpty()) {
                    return@observe
                }
                when (workInfos.first().state) {
                    WorkInfo.State.ENQUEUED -> {
                        Timber.d("DISLIKING PHOTO ENQUEUED")
                    }
                    WorkInfo.State.RUNNING -> {
                        Timber.d("DISLIKING PHOTO RUNNING")
                    }
                    WorkInfo.State.FAILED -> {
                        Timber.d("DISLIKING PHOTO FAILED")
                        WorkManager.getInstance(requireContext()).pruneWork()
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        Timber.d("DISLIKING PHOTO SUCCEEDED")
                        WorkManager.getInstance(requireContext()).pruneWork()
                        updateDataOnFeedDislike()
                    }
                    WorkInfo.State.CANCELLED -> {
                        Timber.d("DISLIKING PHOTO CANCELED")
                        WorkManager.getInstance(requireContext()).pruneWork()
                    }
                    WorkInfo.State.BLOCKED -> {
                        Timber.d("DISLIKING PHOTO BLOCKED")
                    }
                }
            }
    }

    private fun updateDataOnFeedDislike() {
        // getting data from paging adapter`s snapshot
        val snapshotItem = searchAdapter.snapshot().firstOrNull { snapshotItem ->
            snapshotItem?.id == this.id
        }

        snapshotItem?.let {
            // updating snapshot data
            it.likedByUser = false
            it.likes -= 1

            // updating paging data adapter
            searchAdapter.notifyDataSetChanged()
        }
    }
}