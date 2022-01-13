package lt.vitalikas.unsplash.ui.feed_screen

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.*
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.databinding.FragmentFeedBinding
import lt.vitalikas.unsplash.utils.autoCleaned
import timber.log.Timber

@AndroidEntryPoint
class FeedFragment : Fragment(R.layout.fragment_feed) {

    private val binding by viewBinding(FragmentFeedBinding::bind)
    private val feed get() = binding.rvFeed
    private val loading get() = binding.pbLoading

    private val feedViewModel by viewModels<FeedViewModel>()

    private val feedAdapter by autoCleaned {
        FeedAdapter().apply {
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
                        state.error.message?.let { text -> showSnackbar(text) }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFeedPhotosRv()
        bindViewModel()
        getFeed()
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

//            val verticalDividerItemDecor =
//                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
//            addItemDecoration(verticalDividerItemDecor)
//            val horizontalDividerItemDecor =
//                DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
//            addItemDecoration(horizontalDividerItemDecor)
        }
    }

    private fun getFeed() {
        viewLifecycleOwner.lifecycleScope.launch {
            feedViewModel.getFeedPhotos()
        }
    }

    private fun showSnackbar(message: String) {
        Snackbar
            .make(
                requireView(),
                message,
                Snackbar.LENGTH_LONG
            )
            .setAnchorView(R.id.bottom_navigation)
            .setAction("Retry") {
                //
            }
            .show()
    }

    private fun bindViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    feedViewModel.feedState.collect { state ->
                        when (state) {
                            is FeedState.Success -> feedAdapter.submitData(state.data)
                            is FeedState.Error -> Timber.d("${state.error}")
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        feedViewModel.cancelScopeChildrenJobs()
    }
}