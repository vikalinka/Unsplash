package lt.vitalikas.unsplash.ui.feed_screen

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.*
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.FragmentFeedBinding
import timber.log.Timber

@AndroidEntryPoint
class FeedFragment : Fragment(R.layout.fragment_feed) {

    private val binding by viewBinding(FragmentFeedBinding::bind)
    private val title get() = binding.tvTitle
    private val feed get() = binding.rvFeed
    private val loading get() = binding.pbLoading

    private val feedPhotosAdapter
        get() = requireNotNull(feed.adapter as FeedAdapter) {
            error("Feed adapter not initialized")
        }

    private val feedViewModel by viewModels<FeedViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFeedPhotosRv()
        bindViewModel()
        getFeed()
    }

    private fun initFeedPhotosRv() {
        with(feed) {
            adapter = FeedAdapter()

            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)

            addItemDecoration(FeedOffsetDecoration(requireContext()))

//            val verticalDividerItemDecor =
//                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
//            addItemDecoration(verticalDividerItemDecor)
//            val horizontalDividerItemDecor =
//                DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
//            addItemDecoration(horizontalDividerItemDecor)
        }
    }

    private fun getFeed() {
        feedViewModel.getFeedPhotos()
    }

    private fun toggleViewsVisibility(isVisible: Boolean) {
        listOf(title, feed).forEach { view ->
            view.isVisible = !isVisible
        }
        loading.isVisible = isVisible
    }

    private fun showSnackbar() {
        Snackbar
            .make(
                requireView(),
                "There is no connection to the server, saved objects are shown",
                Snackbar.LENGTH_LONG
            )
            .setAnchorView(R.id.bottom_navigation)
            .setAction("Retry") {
                //
            }
            .show()
    }

    private fun bindViewModel() {
        feedViewModel.feedState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FeedPhotosState.Loading -> {
                    toggleViewsVisibility(state.isLoading)
                }
                is FeedPhotosState.Success -> {
                    lifecycleScope.launch {
                        feedPhotosAdapter.submitData(state.data)
                    }
                }
                is FeedPhotosState.Error -> {
                    Timber.d("${state.error}")
                    showSnackbar()
                }
                is FeedPhotosState.Cancellation -> {

                }
            }
        }
    }
}