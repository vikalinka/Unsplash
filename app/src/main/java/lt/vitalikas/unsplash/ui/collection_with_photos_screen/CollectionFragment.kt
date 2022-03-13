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
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatus
import lt.vitalikas.unsplash.databinding.FragmentCollectionBinding
import lt.vitalikas.unsplash.domain.models.collections.Collection
import lt.vitalikas.unsplash.ui.collections_screen.CollectionsLoadStateAdapter
import lt.vitalikas.unsplash.utils.autoCleaned
import lt.vitalikas.unsplash.utils.showInfo
import timber.log.Timber

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
                    CollectionFragmentDirections.actionCollectionFragmentToFeedDetailsFragment(id)
                findNavController().navigate(directions)
            },
            onLikeClick = { id ->
                //
            },
            onDislikeClick = { id ->
                //
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCollectionPhotoList()
        getCollectionWithPhotos(args.id)
        observeFetchingCollection()
        observeFetchingPhotos()
        observeNetworkConnection()
        setupToolbar()
        handleToolbarNavigation()
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
            title = "COLLECTION"

            inflateMenu(R.menu.search_toolbar_menu)
        }
    }

    private fun handleToolbarNavigation() {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}