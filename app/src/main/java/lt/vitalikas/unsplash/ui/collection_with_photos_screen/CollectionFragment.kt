package lt.vitalikas.unsplash.ui.collection_with_photos_screen

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkInfo
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatus
import lt.vitalikas.unsplash.data.services.DislikePhotoWorker
import lt.vitalikas.unsplash.data.services.LikePhotoWorker
import lt.vitalikas.unsplash.databinding.FragmentCollectionBinding
import lt.vitalikas.unsplash.domain.models.collections.Collection
import lt.vitalikas.unsplash.ui.collections_screen.CollectionsLoadStateAdapter
import lt.vitalikas.unsplash.utils.autoCleaned
import lt.vitalikas.unsplash.utils.showInfo
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class CollectionFragment : Fragment(R.layout.fragment_collection) {

    private val binding by viewBinding(FragmentCollectionBinding::bind)
    private val loadingProgress get() = binding.loadingProgressBar
    private val coverPhoto get() = binding.coverPhotoImageView
    private val userPhoto get() = binding.userImageView
    private val userName get() = binding.usernameTextView
    private val userUsername get() = binding.nameTextView
    private val collectionTitle get() = binding.collectionTitleTextView
    private val collectionTags get() = binding.collectionTagsTextView
    private val collectionDescription get() = binding.collectionDescriptionTextView
    private val photoCount get() = binding.photoCountTextView
    private val photoList get() = binding.photosRecyclerView
    private val noConnectionText get() = binding.noConnectionTextView
    private val toolbar get() = binding.toolbar

    private val collectionViewModel by viewModels<CollectionViewModel>()

    private val args by navArgs<CollectionFragmentArgs>()

    private val collectionAdapter by autoCleaned {
        CollectionAdapter(
            onItemClick = { id ->
                val directions =
                    CollectionFragmentDirections.actionCollectionsToDetails2(id)
                findNavController().navigate(directions)
            },
            onLikeClick = { id ->
                this.id = id

                collectionViewModel.likePhoto(id)
            },
            onDislikeClick = { id ->
                this.id = id

                collectionViewModel.dislikePhoto(id)
            }
        )
    }

    private lateinit var id: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCollectionPhotoList()
        setupToolbar()
        getCollectionWithPhotos(args.id)
        observeFetchingCollection()
        observeFetchingPhotos()
        observeNetworkConnection()
        observeLikingPhoto()
        observeDislikingPhoto()
    }

    private fun observeNetworkConnection() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectionViewModel.networkStatus.collect { status ->
                    when (status) {
                        NetworkStatus.Available -> {
                            noConnectionText.isVisible = false
                            // retry after connection re-established
                            collectionAdapter.retry()
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

    private fun getCollectionWithPhotos(id: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectionViewModel.getCollection(id)
            }
        }
    }

    private fun observeFetchingCollection() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectionViewModel.collectionState.collectLatest { state ->
                    when (state) {
                        is CollectionState.Init -> Timber.d("State initialization")
                        is CollectionState.Loading -> loadingProgress.isVisible = true
                        is CollectionState.Error -> {
                            loadingProgress.isVisible = false
                            state.error.message?.let { showInfo(it) }
                        }
                        is CollectionState.Success -> {
                            bindFetchedCollectionData(state.collection)
                            collectionViewModel.getCollectionPhotos(args.id)
                        }
                    }
                }
            }
        }
    }

    private fun bindFetchedCollectionData(collection: Collection) {
        Glide.with(this)
            .load(collection.coverPhoto?.urls?.raw)
            .placeholder(R.drawable.picture)
            .error(R.drawable.picture)
            .into(coverPhoto)

        Glide.with(this)
            .load(collection.user?.profileImage?.medium)
            .placeholder(R.drawable.picture)
            .error(R.drawable.picture)
            .into(userPhoto)

        userName.text = collection.user?.name
        userUsername.text = getString(R.string.username, collection.user?.username)
        collectionTitle.text = collection.title

        collectionTags.text = collection.tags?.joinToString(
            separator = " #",
            prefix = "#"
        ) { it.title }

        collectionDescription.text = collection.description
        photoCount.text = collection.totalPhotos.toString()
    }

    private fun initCollectionPhotoList() {
        with(photoList) {
            val collectionLoadStateAdapter = CollectionsLoadStateAdapter {
                collectionAdapter.retry()
            }

            val concatAdapter = collectionAdapter.withLoadStateFooter(
                footer = collectionLoadStateAdapter
            )

            adapter = concatAdapter

            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )

            setHasFixedSize(true)
        }
    }

    private fun observeFetchingPhotos() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectionViewModel.photosState.collectLatest { state ->
                    when (state) {
                        is CollectionPhotosState.Init -> Timber.d("State initialization")
                        is CollectionPhotosState.Loading -> loadingProgress.isVisible = true
                        is CollectionPhotosState.Error -> {
                            loadingProgress.isVisible = false
                            state.error.message?.let { showInfo(it) }
                        }
                        is CollectionPhotosState.Success -> {
                            loadingProgress.isVisible = false
                            collectionAdapter.submitData(state.photos)
                        }
                    }
                }
            }
        }
    }

    private fun setupToolbar() {
        with(toolbar) {
            title = args.title.uppercase(Locale.getDefault())

            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun observeLikingPhoto() {
        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData(LikePhotoWorker.LIKE_PHOTO_WORK_ID_FROM_COLLECTION)
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
        val snapshotItem = collectionAdapter.snapshot().firstOrNull { snapshotItem ->
            snapshotItem?.id == this.id
        }

        snapshotItem?.let {
            // updating snapshot data
            it.likedByUser = true
            it.likes += 1

            // updating paging data adapter
            collectionAdapter.notifyDataSetChanged()
        }
    }

    private fun observeDislikingPhoto() {
        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData(DislikePhotoWorker.DISLIKE_PHOTO_WORK_ID_FROM_COLLECTION)
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
        val snapshotItem = collectionAdapter.snapshot().firstOrNull { snapshotItem ->
            snapshotItem?.id == this.id
        }

        snapshotItem?.let {
            // updating snapshot data
            it.likedByUser = false
            it.likes -= 1

            // updating paging data adapter
            collectionAdapter.notifyDataSetChanged()
        }
    }
}