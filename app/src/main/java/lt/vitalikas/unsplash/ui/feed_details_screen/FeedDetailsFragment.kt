package lt.vitalikas.unsplash.ui.feed_details_screen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatus
import lt.vitalikas.unsplash.data.services.DislikePhotoWorker
import lt.vitalikas.unsplash.data.services.DownloadPhotoWorker
import lt.vitalikas.unsplash.data.services.LikePhotoWorker
import lt.vitalikas.unsplash.databinding.FragmentFeedDetailsBinding
import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails
import lt.vitalikas.unsplash.ui.rationale_screen.Launcher
import lt.vitalikas.unsplash.utils.hasQ
import lt.vitalikas.unsplash.utils.showInfo
import lt.vitalikas.unsplash.utils.showInfoWithAction
import timber.log.Timber
import kotlin.properties.Delegates

@AndroidEntryPoint
class FeedDetailsFragment : Fragment(R.layout.fragment_feed_details),
    Launcher {

    private val binding by viewBinding(FragmentFeedDetailsBinding::bind)
    private val photo get() = binding.ivPhoto
    private val userPhoto get() = binding.ivAvatar
    private val userName get() = binding.tvName
    private val userUsername get() = binding.tvUsername
    private val userTotalLikes get() = binding.tvLikeCount
    private val likedByUser get() = binding.ivLove
    private val photoDetails get() = binding.rvDetails
    private val loadingProgress get() = binding.pbLoading
    private val noConnectionText get() = binding.tvNoConnection
    private val toolbar get() = binding.toolbar

    private val feedDetailsViewModel by viewModels<FeedDetailsViewModel>()

    private val args by navArgs<FeedDetailsFragmentArgs>()

    private lateinit var photoShareLink: String

    private var likeCount by Delegates.notNull<Int>()

    private val feedPhotoDetailsAdapter
        get() = requireNotNull(photoDetails.adapter as FeedDetailsAdapter) {
            error("Adapter not initialized")
        }

    private lateinit var photoDownloadUrl: String
    private lateinit var photoUri: Uri

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsWithStatuses ->
        if (permissionsWithStatuses.values.all { isGranted ->
                isGranted == true
            }) {
            savePhotoInSelectedFolderLauncher.launch("${args.id}.jpg")
        } else {
            if (isNeedToShowRationale()) {
                showPermissionRationaleDialog()
            } else {
                showInfo(R.string.perm_all)
            }
        }
    }

    private val savePhotoInSelectedFolderLauncher = registerForActivityResult(
        CreateDocument(MIME_TYPE)
    ) { uri ->
        uri?.let {
            photoUri = it
            feedDetailsViewModel.downloadPhoto(photoDownloadUrl, it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPhotoDetails()
        /**
         * DeepLink
         * https://unsplash.com/photos/4oovIxttThA
         * Implicit DeepLink uses args parameter for automatic parsing
         */
        getFeedPhotoDetails(args.id)
        setupToolbar()
        observeDataFetching()
        observeNetworkConnection()
        observeDownload()
        observeLikingPhoto()
        observeDislikingPhoto()
        handleToolbarNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()
        feedDetailsViewModel.cancelScopeChildrenJobs()
    }

    override fun onGrantButtonClick() {
        requestPermissions()
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
                        is FeedDetailsState.Loading -> {
                            loadingProgress.isVisible = true
                        }
                        is FeedDetailsState.Success -> {
                            loadingProgress.isVisible = false
                            feedPhotoDetailsAdapter.items = listOf(state.data)
                            bindFetchedData(state.data)
                            photoShareLink = state.data.links.html
                        }
                        is FeedDetailsState.Error -> {
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

    private fun bindFetchedData(details: FeedPhotoDetails) {
        Glide.with(this)
            .load(details.urls.raw)
            .placeholder(R.drawable.picture)
            .error(R.drawable.picture)
            .into(photo)

        Glide.with(this)
            .load(details.user.profileImage.medium)
            .placeholder(R.drawable.picture)
            .error(R.drawable.picture)
            .into(userPhoto)

        userName.text = details.user.name

        userUsername.text = getString(R.string.username, details.user.username)

        userTotalLikes.text = details.likes.toString()
        likeCount = details.likes

        with(likedByUser) {
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
    }

    private fun initPhotoDetails() {
        with(photoDetails) {
            adapter = FeedDetailsAdapter(
                onLocationClick = { lat, lng ->
                    val locationUri = Uri.parse("geo: $lat,$lng")
                    showLocationInMap(locationUri)
                },
                onDownloadClick = { url ->
                    photoDownloadUrl = url
                    checkPermissions()
                }
            )
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
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

    private fun openPhotoUri(uri: Uri) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = MIME_TYPE
        }

        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }

    private fun observeLikingPhoto() {
        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData(LikePhotoWorker.LIKE_PHOTO_WORK_ID_FROM_DETAILS)
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
        likedByUser.setImageResource(R.drawable.ic_love_filled)
        userTotalLikes.text = (likeCount + 1).toString()
    }

    private fun observeDislikingPhoto() {
        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData(DislikePhotoWorker.DISLIKE_PHOTO_WORK_ID_FROM_DETAILS)
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
        likedByUser.setImageResource(R.drawable.ic_love)
        userTotalLikes.text = (likeCount - 1).toString()
    }

    private fun observeDownload() {
        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData(DownloadPhotoWorker.DOWNLOAD_PHOTO_WORK_ID)
            .observe(viewLifecycleOwner) { workInfos ->
                if (workInfos.isNullOrEmpty()) {
                    return@observe
                }
                when (workInfos.first().state) {
                    WorkInfo.State.ENQUEUED -> {
                        Timber.d("DOWNLOAD ENQUEUED")
                    }
                    WorkInfo.State.RUNNING -> {
                        Timber.d("DOWNLOAD RUNNING")
                    }
                    WorkInfo.State.FAILED -> {
                        Timber.d("DOWNLOAD FAILED")
                        WorkManager.getInstance(requireContext()).pruneWork()
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        Timber.d("DOWNLOAD SUCCEEDED")
                        WorkManager.getInstance(requireContext()).pruneWork()
                        showInfoWithAction(
                            R.string.download_succeeded,
                            R.string.open
                        ) {
                            openPhotoUri(photoUri)
                        }
                    }
                    WorkInfo.State.CANCELLED -> {
                        Timber.d("DOWNLOAD CANCELED")
                        WorkManager.getInstance(requireContext()).pruneWork()
                    }
                    WorkInfo.State.BLOCKED -> {
                        Timber.d("DOWNLOAD BLOCKED")
                    }
                }
            }
    }

    private fun handleToolbarNavigation() {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun checkPermissions() {
        if (hasAllPermissions().not()) {
            requestPermissions()
        } else {
            savePhotoInSelectedFolderLauncher.launch("${args.id}.jpg")
        }
    }

    private fun hasAllPermissions(): Boolean = PERMISSIONS.all { permission ->
        ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() =
        requestPermissionLauncher.launch(PERMISSIONS.toTypedArray())

    private fun isNeedToShowRationale(): Boolean = PERMISSIONS.any { permission ->
        isNeedRationaleForPermission(permission)
    }

    private fun isNeedRationaleForPermission(permission: String) =
        ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            permission
        )

    private fun showPermissionRationaleDialog() =
        findNavController().navigate(FeedDetailsFragmentDirections.actionFeedDetailsFragmentToRationaleFragment())

    private companion object {
        val PERMISSIONS = listOfNotNull(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE.takeIf {
                hasQ().not()
            }
        )

        const val MIME_TYPE = "image/jpeg"
    }
}