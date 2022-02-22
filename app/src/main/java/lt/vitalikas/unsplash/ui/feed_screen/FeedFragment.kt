package lt.vitalikas.unsplash.ui.feed_screen

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
import androidx.paging.LoadState
import androidx.recyclerview.widget.*
import androidx.work.WorkInfo
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatus
import lt.vitalikas.unsplash.data.services.DislikePhotoWorker
import lt.vitalikas.unsplash.data.services.LikePhotoWorker
import lt.vitalikas.unsplash.databinding.FragmentFeedBinding
import lt.vitalikas.unsplash.utils.autoCleaned
import lt.vitalikas.unsplash.utils.onTextChangedFlow
import lt.vitalikas.unsplash.utils.showInfo
import timber.log.Timber

@AndroidEntryPoint
class FeedFragment : Fragment(R.layout.fragment_feed) {

    private val binding by viewBinding(FragmentFeedBinding::bind)
    private val feed get() = binding.rvFeed
    private val noConnection get() = binding.tvNoConnection
    private val loading get() = binding.pbLoading
    private val refresh get() = binding.srl
    private val toolbar get() = binding.toolbar

    private val feedViewModel by viewModels<FeedViewModel>()

    private lateinit var id: String

    private val feedAdapter by autoCleaned {
        FeedAdapter(
            onItemClick = { id ->
                val directions = FeedFragmentDirections.actionHomeToFeedDetailsFragment(id)
                findNavController().navigate(directions)
            },
            onLikeClick = { id ->
                this.id = id

                feedViewModel.likePhoto(id)
            },
            onDislikeClick = { id ->
                this.id = id

                feedViewModel.dislikePhoto(id)
            }
        ).apply {
            addLoadStateListener { loadStates ->
                if (loadStates.refresh is LoadState.Loading) {
                    loading.isVisible = true
                } else {
                    loading.isVisible = false

                    val errorState = when {
                        loadStates.prepend is LoadState.Error -> loadStates.prepend as LoadState.Error
                        loadStates.append is LoadState.Error -> loadStates.append as LoadState.Error
                        loadStates.refresh is LoadState.Error -> loadStates.refresh as LoadState.Error
                        else -> null
                    }

                    errorState?.let { state ->
                        state.error.message?.let { text -> showInfo(requireView(), text) }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFeedPhotosRv()
        bindViewModel()
        getData()
        setListeners()
        setupToolbar()
        observeLikingPhoto()
        observeDislikingPhoto()
    }

    override fun onPause() {
        super.onPause()
        feedViewModel.cancelScopeChildrenJobs()
    }

    private fun initFeedPhotosRv() {
        with(feed) {
            val concatAdapter = feedAdapter.withLoadStateFooter(
                footer = FeedLoadStateAdapter()
            )

            adapter = concatAdapter

            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            setHasFixedSize(true)

            addItemDecoration(FeedOffsetDecoration(requireContext()))
        }
    }

    private fun getData() {
        viewLifecycleOwner.lifecycleScope.launch {
            feedViewModel.getFeedPhotos()
        }
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    feedViewModel.feedState.collect { state ->
                        when (state) {
                            is FeedState.Success -> {
                                feedAdapter.submitData(state.data)
                                refresh.isRefreshing = false
                            }
                            is FeedState.Error -> {
                                refresh.isRefreshing = false
                                state.error.message?.let {
                                    showInfo(
                                        requireView(),
                                        it
                                    )
                                }
                                Timber.d("${state.error}")
                            }
                        }
                    }
                }

                launch {
                    feedViewModel.networkStatus.collect { status ->
                        when (status) {
                            NetworkStatus.Available -> {
                                noConnection.isVisible = false
                                // retry after connection re-established
                                feedAdapter.retry()
                            }
                            NetworkStatus.Unavailable -> {
                                noConnection.isVisible = true
                                showInfo(
                                    requireView(),
                                    "No internet connection. Cached data is shown."
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setListeners() {
        refresh.setOnRefreshListener {
            feedAdapter.refresh()
        }
    }

    private fun setupToolbar() {
        with(toolbar) {
            title = getString(R.string.nav_feed)

            inflateMenu(R.menu.feed_toolbar_menu)

            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.toolbar_menu_search -> {

                        menuItem.setOnActionExpandListener(object :
                            MenuItem.OnActionExpandListener {
                            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                                showToast("search expanded")
                                return true
                            }

                            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                                showToast("search collapsed")
                                return true
                            }
                        })

                        with(menuItem.actionView as SearchView) {
                            feedViewModel.searchFeedPhotos(this.onTextChangedFlow())
                            isIconified = false
                        }

                        true
                    }

                    else -> false
                }
            }
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
        val snapshotItem = feedAdapter.snapshot().firstOrNull { snapshotItem ->
            snapshotItem?.id == this.id
        }

        snapshotItem?.let {
            // updating snapshot data
            it.likedByUser = true
            it.likes += 1

            // updating paging data adapter
            feedAdapter.notifyDataSetChanged()

            // updating database data
            feedViewModel.updatePhotoInDatabase(it.id, true, it.likes)
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
        val snapshotItem = feedAdapter.snapshot().firstOrNull { snapshotItem ->
            snapshotItem?.id == this.id
        }

        snapshotItem?.let {
            // updating snapshot data
            it.likedByUser = false
            it.likes -= 1

            // updating paging data adapter
            feedAdapter.notifyDataSetChanged()

            // updating database data
            feedViewModel.updatePhotoInDatabase(it.id, false, it.likes)
        }
    }
}