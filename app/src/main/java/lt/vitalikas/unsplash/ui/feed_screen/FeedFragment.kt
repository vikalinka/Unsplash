package lt.vitalikas.unsplash.ui.feed_screen

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatus
import lt.vitalikas.unsplash.data.services.photo_service.DislikePhotoWorker
import lt.vitalikas.unsplash.data.services.photo_service.LikePhotoWorker
import lt.vitalikas.unsplash.databinding.FragmentFeedBinding
import lt.vitalikas.unsplash.utils.autoCleaned
import lt.vitalikas.unsplash.utils.showInfo
import timber.log.Timber

@AndroidEntryPoint
class FeedFragment : Fragment(R.layout.fragment_feed) {

    private val binding by viewBinding(FragmentFeedBinding::bind)
    private val photoList get() = binding.rvFeed
    private val noConnectionText get() = binding.tvNoConnection
    private val loadingProgress get() = binding.pbLoading
    private val refreshLayout get() = binding.srl
    private val toolbar get() = binding.toolbar

    private val feedViewModel by activityViewModels<FeedViewModel>()

    private lateinit var id: String

    private val feedAdapter by autoCleaned {
        PhotoAdapter(
            onItemClick = { id ->
                val directions = FeedFragmentDirections.actionFeedToDetails1(id)
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
            /**
             * After updating photo data in database and navigating up from detailed screen RV scrolls to the top.
             * To avoid this, need to use stateRestorationPolicy.
             */
            this.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        initPhotoList()
        initListRefresh()
        getData(feedViewModel.prevOrderBy)
        observeData()
        observeNetworkConnection()
        observeAdapterLoadingState()
        observeLikingPhoto()
        observeDislikingPhoto()
    }

    private fun initPhotoList() {
        with(photoList) {
            val feedLoadStateAdapter = PhotoLoadStateAdapter(
                onRetryButtonClick = { feedAdapter.retry() }
            )

            val concatAdapter = feedAdapter.withLoadStateFooter(
                footer = feedLoadStateAdapter
            )

            adapter = concatAdapter

            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

            setHasFixedSize(true)

            addItemDecoration(PhotoOffsetDecoration(requireContext()))
        }
    }

    private fun getData(order: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            feedViewModel.getFeedPhotos(order)
        }
    }

    private fun observeAdapterLoadingState() {
        viewLifecycleOwner.lifecycleScope.launch {
            feedAdapter.loadStateFlow.collectLatest { loadStates ->

                loadingProgress.isVisible = loadStates.refresh is LoadState.Loading
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                feedViewModel.feedState.collectLatest { state ->
                    when (state) {
                        is FeedState.Success -> {
                            refreshLayout.isRefreshing = false
                            feedAdapter.submitData(state.data)
                        }
                        is FeedState.Error -> {
                            refreshLayout.isRefreshing = false
                            loadingProgress.isVisible = false
                            state.error.message?.let {
                                showInfo(it)
                            }
                            Timber.d("${state.error}")
                        }
                    }
                }
            }
        }
    }

    private fun observeNetworkConnection() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                feedViewModel.networkStatus.collect { status ->
                    when (status) {
                        NetworkStatus.Available -> {
                            noConnectionText.isVisible = false
                            // retry after connection re-established
                            feedAdapter.retry()
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

    private fun initListRefresh() {
        refreshLayout.setOnRefreshListener {
            feedAdapter.refresh()
        }
    }

    private fun setupToolbar() {
        with(toolbar) {
            title = getString(R.string.nav_feed)

            inflateMenu(R.menu.feed_toolbar_menu)

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.orderByLatestAction -> {
                        getData(ORDER_BY_LATEST)
                        true
                    }
                    R.id.orderByOldestAction -> {
                        getData(ORDER_BY_OLDEST)
                        true
                    }
                    R.id.orderByPopularAction -> {
                        getData(ORDER_BY_POPULAR)
                        true
                    }
                    else -> {
                        val directions =
                            FeedFragmentDirections.actionFeedToSearch()
                        findNavController().navigate(directions)
                        true
                    }
                }
            }
        }
    }

    private fun observeLikingPhoto() {
        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData(LikePhotoWorker.LIKE_PHOTO_WORK_ID_FEED)
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
                        updateDataOnPhotoReaction(true)
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

    private fun observeDislikingPhoto() {
        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData(DislikePhotoWorker.DISLIKE_PHOTO_WORK_ID_FEED)
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
                        updateDataOnPhotoReaction(false)
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

    private fun updateDataOnPhotoReaction(reaction: Boolean) {

        // getting data from paging adapter`s snapshot
        val photo = feedAdapter.snapshot().firstOrNull { snapshotItem ->
            snapshotItem?.id == this.id
        }

        photo?.let {
            /**
             * Updating single item in list using its snapshot.
             * Not recommended.
             */

//            // updating snapshot data
//            it.likedByUser = true
//            it.likes += 1
//            // updating paging data adapter
//            feedAdapter.notifyDataSetChanged()

            val id = it.id

            val totalLikes = if (reaction) {
                it.likes + 1
            } else {
                it.likes - 1
            }

            /**
             * Updating single item using Flow combine method.
             * Combining paging data flow with local changes flow.
             */
            // updating local changes
            feedViewModel.updateLocalChanges(id, reaction, totalLikes)

            // updating database data
            feedViewModel.updatePhotoInDatabase(id, reaction, totalLikes)
        }
    }

    companion object {
        private const val ORDER_BY_LATEST = "latest"
        private const val ORDER_BY_OLDEST = "oldest"
        private const val ORDER_BY_POPULAR = "popular"
    }
}