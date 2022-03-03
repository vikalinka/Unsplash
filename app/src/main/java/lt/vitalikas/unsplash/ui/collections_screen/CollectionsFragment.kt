package lt.vitalikas.unsplash.ui.collections_screen

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import lt.vitalikas.unsplash.R
import lt.vitalikas.unsplash.data.networking.status_tracker.NetworkStatus
import lt.vitalikas.unsplash.databinding.FragmentCollectionsBinding
import lt.vitalikas.unsplash.utils.autoCleaned
import lt.vitalikas.unsplash.utils.showInfo
import timber.log.Timber

@AndroidEntryPoint
class CollectionsFragment : Fragment(R.layout.fragment_collections) {

    private val binding by viewBinding(FragmentCollectionsBinding::bind)
    private val collectionsList get() = binding.collectionsRecyclerView
    private val noConnectionText get() = binding.tvNoConnection
    private val loadingProgress get() = binding.pbLoading
    private val refreshLayout get() = binding.collectionsSwipeRefreshLayout
    private val toolbar get() = binding.collectionsToolbar

    private val collectionsViewModel by viewModels<CollectionsViewModel>()

    private lateinit var id: String

    private val collectionAdapter by autoCleaned {
        CollectionsAdapter(
            onItemClick = { id ->
                val directions =
                    CollectionsFragmentDirections.actionCollectionsToCollectionFragment(id)
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
        setupToolbar()
        initCollectionsList()
        initAdapterRefresh()
        observeSearchResults()
        observeNetworkConnection()
    }

    private fun initCollectionsList() {
        with(collectionsList) {
            val collectionLoadStateAdapter = CollectionsLoadStateAdapter {
                collectionAdapter.retry()
            }

            val concatAdapter = collectionAdapter.withLoadStateFooter(
                footer = collectionLoadStateAdapter
            )

            adapter = concatAdapter

            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

            setHasFixedSize(true)

            addItemDecoration(CollectionsOffsetDecoration(requireContext()))
        }
    }

    private fun observeSearchResults() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    collectionsViewModel.getCollections()
                        .collectLatest { data ->
                            Timber.d("$data")
                            collectionAdapter.submitData(data)
                            refreshLayout.isRefreshing = false
                        }
                }
            }
        }
    }

    private fun observeNetworkConnection() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    collectionsViewModel.networkStatus.collect { status ->
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
    }

    private fun initAdapterRefresh() {
        refreshLayout.setOnRefreshListener {
            collectionAdapter.refresh()
        }
    }

    private fun setupToolbar() {
        with(toolbar) {
            title = "COLLECTIONS"

            inflateMenu(R.menu.toolbar_menu)
        }
    }
}