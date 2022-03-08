package lt.vitalikas.unsplash.ui.feed_screen

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
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
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPhotoList()
        getData()
        observeDataFetching()
        observeNetworkConnection()
        initListRefresh()
        setupToolbar()
        observeLikingPhoto()
        observeDislikingPhoto()
    }

    private fun initPhotoList() {
        with(photoList) {
            val feedLoadStateAdapter = FeedLoadStateAdapter {
                feedAdapter.retry()
            }

            val concatAdapter = feedAdapter.withLoadStateFooter(
                footer = feedLoadStateAdapter
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

    private fun observeDataFetching() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                feedViewModel.feedState.collect { state ->
                    when (state) {
                        is FeedState.Loading -> loadingProgress.isVisible = true
                        is FeedState.Success -> {
                            loadingProgress.isVisible = false
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

            val findItem = menu.findItem(R.id.findAction)
            findItem.setOnMenuItemClickListener {

                val directions =
                    FeedFragmentDirections.actionHomeToSearchFragment()
                findNavController().navigate(directions)

                return@setOnMenuItemClickListener true
            }
        }
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