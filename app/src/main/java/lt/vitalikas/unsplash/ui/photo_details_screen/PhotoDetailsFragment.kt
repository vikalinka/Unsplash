package lt.vitalikas.unsplash.ui.photo_details_screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatus
import lt.vitalikas.unsplash.data.services.photo_service.DislikePhotoWorker
import lt.vitalikas.unsplash.data.services.photo_service.DownloadPhotoWorker
import lt.vitalikas.unsplash.data.services.photo_service.LikePhotoWorker
import lt.vitalikas.unsplash.databinding.FragmentFeedDetailsBinding
import lt.vitalikas.unsplash.domain.models.photo_details.PhotoDetails
import lt.vitalikas.unsplash.ui.feed_screen.FeedViewModel
import lt.vitalikas.unsplash.ui.feed_screen.PhotoWithPermissionDownloader
import lt.vitalikas.unsplash.ui.host_screen.OnPhotoDownloadCallback
import lt.vitalikas.unsplash.ui.rationale_screen.OnGrantButtonClickCallback
import lt.vitalikas.unsplash.utils.observePhotoDownload
import lt.vitalikas.unsplash.utils.observePhotoReaction
import lt.vitalikas.unsplash.utils.showInfo
import lt.vitalikas.unsplash.utils.showInfoWithAction
import timber.log.Timber
import kotlin.properties.Delegates

@AndroidEntryPoint
class PhotoDetailsFragment : Fragment(R.layout.fragment_feed_details),
    OnGrantButtonClickCallback, OnPhotoDownloadCallback {

    private val binding by viewBinding(FragmentFeedDetailsBinding::bind)
    private val photo get() = binding.photoImageView
    private val avatar get() = binding.avatarShapeableImageView
    private val name get() = binding.nameTextView
    private val username get() = binding.usernameTextView
    private val totalLikes get() = binding.likeCountTextView
    private val liked get() = binding.loveImageView
    private val locationIcon get() = binding.locationImageView
    private val locationInfo get() = binding.locationTextView
    private val tag get() = binding.tagTextView
    private val madeWith get() = binding.madeWithTextView
    private val model get() = binding.modelTextView
    private val exposure get() = binding.exposureTextView
    private val aperture get() = binding.apertureTextView
    private val focalLength get() = binding.focalLengthTextView
    private val iso get() = binding.isoTextView
    private val about get() = binding.aboutTextView
    private val download get() = binding.downloadTextView
    private val downloadCount get() = binding.downloadCountTextView
    private val downloadIcon get() = binding.downloadCountImageView
    private val loadingProgress get() = binding.loadingProgressBar
    private val noConnectionText get() = binding.noConnectionTextView
    private val toolbar get() = binding.toolbar

    private val feedDetailsViewModel by viewModels<PhotoDetailsViewModel>()

    private val photoViewModel by activityViewModels<FeedViewModel>()

    private val args by navArgs<PhotoDetailsFragmentArgs>()

    private lateinit var photoShareLink: String

    private var likeCount by Delegates.notNull<Int>()

    private lateinit var photoDownloadUrl: String
    private lateinit var photoUri: Uri

    private val photoWithPermissionDownloader: PhotoWithPermissionDownloader
        get() = requireActivity() as PhotoWithPermissionDownloader

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /**
         * DeepLink
         * https://unsplash.com/photos/4oovIxttThA
         * Implicit DeepLink uses args parameter for automatic parsing
         */

        getFeedPhotoDetails(args.id)
        setupToolbar()
        observeDataFetching()
        observeNetworkConnection()

        observePhotoReaction(
            listOf(
                LikePhotoWorker.LIKE_PHOTO_WORK_ID_DETAILS,
                DislikePhotoWorker.DISLIKE_PHOTO_WORK_ID_DETAILS
            ),
        ) { reaction ->
            updateDataOnPhotoReaction(reaction)
        }

        observePhotoDownload(
            listOf(DownloadPhotoWorker.DOWNLOAD_PHOTO_WORK_ID)
        ) {
            showInfoWithAction(
                R.string.download_succeeded,
                R.string.open
            ) {
                sharePhoto(photoUri)
            }
        }

        handleToolbarNavigation()
    }

    override fun onGrantButtonClick() {
        photoWithPermissionDownloader.fetchPhotoWithPermission(args.id, photoDownloadUrl)
    }

    private fun getFeedPhotoDetails(id: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            feedDetailsViewModel.getFeedPhotoDetails(id)
        }
    }

    private fun observeDataFetching() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                feedDetailsViewModel.feedDetailsState.collect { state ->
                    when (state) {
                        is PhotoDetailsFetchingState.Loading -> {
                            loadingProgress.isVisible = true
                        }
                        is PhotoDetailsFetchingState.Success -> {
                            loadingProgress.isVisible = false
                            locationIcon.isVisible = true
                            download.isVisible = true
                            downloadIcon.isVisible = true

                            bindFetchedData(state.data)
                        }
                        is PhotoDetailsFetchingState.Error -> {
                            loadingProgress.isVisible = false
                            state.error.message?.let { showInfo(it) }
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
                feedDetailsViewModel.networkStatus.collect { status ->
                    when (status) {
                        NetworkStatus.Available -> {
                            noConnectionText.isVisible = false
                        }
                        NetworkStatus.Unavailable -> {
                            noConnectionText.isVisible = true
                            showInfo(R.string.no_internet)
                        }
                    }
                }
            }
        }
    }

    private fun bindFetchedData(details: PhotoDetails) {
        Glide.with(this)
            .load(details.url.raw)
            .placeholder(R.drawable.picture)
            .error(R.drawable.picture)
            .into(photo)

        Glide.with(this)
            .load(details.user.profileImage.medium)
            .placeholder(R.drawable.picture)
            .error(R.drawable.picture)
            .into(avatar)

        name.text = details.user.name
        username.text = getString(R.string.username, details.user.username)
        details.likes.also {
            likeCount = it
            totalLikes.text = it.toString()
        }

        with(liked) {
            if (details.likedByUser) {
                setImageResource(R.drawable.ic_love_filled)
                setColorFilter(ContextCompat.getColor(context, R.color.red))
                setOnClickListener {
                    feedDetailsViewModel.dislikePhoto(details.id)
                }
            } else {
                setImageResource(R.drawable.ic_love)
                setColorFilter(ContextCompat.getColor(context, R.color.red))
                setOnClickListener {
                    feedDetailsViewModel.likePhoto(details.id)
                }
            }
        }

        val onLocationClick: (lat: Double, lng: Double) -> Unit = { lat, lng ->
            val locationUri = Uri.parse("geo: $lat,$lng")
            showLocationInMap(locationUri)
        }

        val onDownloadClick: () -> Unit = {
            photoWithPermissionDownloader.fetchPhotoWithPermission(args.id, photoDownloadUrl)
        }

        val lat = details.location.position.latitude
        val lng = details.location.position.longitude

        locationIcon.setOnClickListener {
            if (lat != null && lng != null) {
                onLocationClick(lat, lng)
            }
        }

        locationInfo.text = getString(
            R.string.location,
            details.location.country ?: "N/A", details.location.city ?: "N/A"
        )

        tag.text = getString(R.string.tag, details.tags.joinToString { tag ->
            "#${tag.title ?: "N/A"}"
        })

        madeWith.text = getString(R.string.made_with, details.exif.make)
        model.text = getString(R.string.model, details.exif.model)
        exposure.text = getString(R.string.exposure, details.exif.exposureTime)
        aperture.text = getString(R.string.aperture, details.exif.aperture)
        focalLength.text = getString(R.string.focal_length, details.exif.focalLength.toString())
        iso.text = getString(R.string.iso, details.exif.iso.toString())
        about.text = getString(
            R.string.about,
            details.user.username,
            details.user.bio ?: "N/A"
        )

        download.setOnClickListener {
            onDownloadClick()
        }
        downloadCount.text = details.downloads.toString()

        photoShareLink = details.link.html
        photoDownloadUrl = details.link.downloadLocation
    }

    private fun showLocationInMap(location: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = location
        }
        if (activity?.packageManager != null) {
            startActivity(intent)
        }
    }

    private fun setupToolbar() {
        with(toolbar) {
            title = getString(R.string.feed_photo)

            inflateMenu(R.menu.feed_details_toolbar_menu)

            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.toolbar_menu_share -> {
                        sharePhotoLink()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun sharePhotoLink() {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, photoShareLink)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }

    private fun sharePhoto(uri: Uri) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = MIME_TYPE
        }

        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }

    private fun updateDataOnPhotoReaction(reaction: Boolean) {
        val id = args.id
        val newLikeCount = if (reaction) {
            likeCount + 1
        } else {
            likeCount - 1
        }

        totalLikes.text = newLikeCount.toString()

        with(liked) {
            if (reaction) {
                liked.setImageResource(R.drawable.ic_love_filled)
                setOnClickListener { feedDetailsViewModel.dislikePhoto(args.id) }
            } else {
                liked.setImageResource(R.drawable.ic_love)
                setOnClickListener { feedDetailsViewModel.likePhoto(args.id) }
            }
        }

        photoViewModel.updateLocalChanges(
            id = id,
            isLiked = reaction,
            likeCount = newLikeCount
        )

        photoViewModel.updatePhotoInDatabase(
            id = id,
            isLiked = reaction,
            likeCount = newLikeCount
        )
    }

    private fun handleToolbarNavigation() {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private companion object {
        const val MIME_TYPE = "image/jpeg"
    }

    override fun getLocationUri(uri: Uri) {
        photoUri = uri
    }
}