package lt.vitalikas.unsplash.ui.collection_with_photos_screen

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.FragmentCollectionBinding
import lt.vitalikas.unsplash.domain.models.collections.Collection
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
    private val collectionDescription get() = binding.collectionDescriptionTextView
    private val photoCount get() = binding.photoCountTextView

    private val collectionViewModel by viewModels<CollectionViewModel>()

    private val args by navArgs<CollectionFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getCollectionWithPhotos(args.id)
        observeFetchingCollection()
    }

    private fun getCollectionWithPhotos(id: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectionViewModel.getCollection(id)
                collectionViewModel.getCollectionPhotos(id)
            }
        }
    }

    private fun observeFetchingCollection() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectionViewModel.collectionState
                    .collectLatest { state ->
                        when (state) {
                            is CollectionState.Init -> Timber.d("State initialization")
                            is CollectionState.Loading -> loadingProgress.isVisible = true
                            is CollectionState.Error -> {
                                loadingProgress.isVisible = false
                                state.error.message?.let { showInfo(it) }
                            }
                            is CollectionState.Success -> {
                                loadingProgress.isVisible = false
                                bindFetchedCollectionData(state.collection)
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
        collectionDescription.text = collection.description
        photoCount.text = collection.totalPhotos.toString()
    }
}