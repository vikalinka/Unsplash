package lt.vitalikas.unsplash.ui.feed_details_screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatus
import lt.vitalikas.unsplash.databinding.FragmentFeedDetailsBinding
import lt.vitalikas.unsplash.domain.models.FeedPhotoDetails
import timber.log.Timber

@AndroidEntryPoint
class FeedDetailsFragment : Fragment(R.layout.fragment_feed_details) {

    private val binding by viewBinding(FragmentFeedDetailsBinding::bind)
    private val photo get() = binding.ivPhoto
    private val userPhoto get() = binding.ivAvatar
    private val userName get() = binding.tvName
    private val userUsername get() = binding.tvUsername
    private val userTotalLikes get() = binding.tvLikeCount
    private val likedByUser get() = binding.ivLove
    private val details get() = binding.rvDetails
    private val progress get() = binding.pbLoading
    private val noConnection get() = binding.tvNoConnection

    private val feedDetailsViewModel by viewModels<FeedDetailsViewModel>()

    private val args by navArgs<FeedDetailsFragmentArgs>()

    private val feedPhotoDetailsAdapter
        get() = requireNotNull(details.adapter as FeedDetailsAdapter) {
            error("Adapter not initialized")
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFeedPhotoDetailsRv()
        getFeedPhotoDetails(args.id)
        bindViewModel()
    }

    private fun getFeedPhotoDetails(id: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            feedDetailsViewModel.getFeedPhotoDetails(id)
        }
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    feedDetailsViewModel.feedDetailsState.collect { state ->
                        when (state) {
                            is FeedDetailsState.Loading -> {
                                progress.isVisible = true
                            }
                            is FeedDetailsState.Success -> {
                                progress.isVisible = false
                                feedPhotoDetailsAdapter.items = listOf(state.data)
                                bind(state.data)
                            }
                            is FeedDetailsState.Error -> {
                                progress.isVisible = false
                                state.error.message?.let { showSnackbar(it) }
                                Timber.d("${state.error}")
                            }
                        }
                    }
                }

                launch {
                    feedDetailsViewModel.networkStatus.collect { status ->
                        when (status) {
                            NetworkStatus.Available -> {
                                noConnection.isVisible = false
                            }
                            NetworkStatus.Unavailable -> {
                                noConnection.isVisible = true
                                showSnackbar("No internet connection. Cached data is shown.")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun bind(details: FeedPhotoDetails) {

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

        userTotalLikes.text = details.user.totalLikes.toString()

        likedByUser.run {
            setImageResource(R.drawable.ic_love_filled)
            setColorFilter(ContextCompat.getColor(context, R.color.red))
        }.takeIf { details.likedByUser } ?: binding.ivLove.run {
            setImageResource(R.drawable.ic_love)
            setColorFilter(ContextCompat.getColor(context, R.color.red))
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar
            .make(
                requireView(),
                message,
                Snackbar.LENGTH_LONG
            )
            .show()
    }

    private fun initFeedPhotoDetailsRv() {
        with(details) {
            adapter = FeedDetailsAdapter { lat, lng ->
                val locationUri = Uri.parse("geo: $lat,$lng")
                showLocationOnMap(locationUri)
            }
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun showLocationOnMap(location: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = location
        }
        if (activity?.packageManager != null) {
            startActivity(intent)
        }
    }
}